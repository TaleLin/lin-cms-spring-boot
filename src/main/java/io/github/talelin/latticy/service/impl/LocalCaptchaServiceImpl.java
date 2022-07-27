package io.github.talelin.latticy.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.github.talelin.latticy.bo.LoginCaptchaBO;
import io.github.talelin.latticy.common.configuration.LoginCaptchaProperties;
import io.github.talelin.latticy.common.util.CaptchaUtil;
import io.github.talelin.latticy.service.CaptchaService;
import io.github.talelin.latticy.vo.LoginCaptchaVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * 验证码AES服务实现类
 *
 * @author Gadfly
 */
@Slf4j
@Service
public class LocalCaptchaServiceImpl implements CaptchaService {
    /**
     * 验证码字符个数
     */
    protected static final int RANDOM_STR_NUM = 4;
    protected static final SecureRandom RANDOM = new SecureRandom();
    /**
     * 验证码的宽
     */
    protected static final int WIDTH = 80;
    /**
     * 验证码的高
     */
    protected static final int HEIGHT = 40;
    protected static final String RANDOM_STRING = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWSYZ";
    protected static final String AES = "AES";
    protected static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    protected static final Cache<String, String> BLACK_LIST =
            CacheBuilder.newBuilder().maximumSize(1000).expireAfterAccess(5, TimeUnit.MINUTES).build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LoginCaptchaProperties captchaConfig;

    /**
     * 字体的设置
     */
    private static Font getFont() throws IOException, FontFormatException {
        ClassPathResource dejavuSerifBold = new ClassPathResource("DejaVuSerif-Bold.ttf");
        return Font.createFont(Font.TRUETYPE_FONT, dejavuSerifBold.getInputStream()).deriveFont(Font.BOLD, 24);
    }

    @PostConstruct
    public void init() {
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }

    @Override
    @SneakyThrows
    public LoginCaptchaVO generateCaptcha() {
        String code = this.getRandomString(RANDOM_STR_NUM);
        String base64String = this.getRandomCodeBase64(code);
        String tag = this.getTag(code, captchaConfig.getSecret(), captchaConfig.getIv());
        return new LoginCaptchaVO(tag, "data:image/png;base64," + base64String);
    }

    @Override
    public boolean verify(String captcha, String tag) {
        if (!this.checkCaptchaTagValid(tag)) {
            return false;
        }
        // 校验完成后可以确认tag可以被解析，此时无论校验成功失败，都要将验证码标识加入黑名单
        this.invalidateCaptchaTag(tag);
        try {
            LoginCaptchaBO captchaBO = this.decodeTag(captchaConfig.getSecret(), captchaConfig.getIv(), tag);
            return captcha.equalsIgnoreCase(captchaBO.getCaptcha());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean checkCaptchaTagValid(String tag) {
        if (BLACK_LIST.getIfPresent(tag) != null) {
            return false;
        }
        try {
            LoginCaptchaBO captchaBO = this.decodeTag(captchaConfig.getSecret(), captchaConfig.getIv(), tag);
            return System.currentTimeMillis() < captchaBO.getExpired();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void invalidateCaptchaTag(String tag) {
        BLACK_LIST.put(tag, tag);
    }

    protected String getTag(String captcha, String secret, String iv) throws JsonProcessingException, GeneralSecurityException {
        LocalDateTime time = LocalDateTime.now().plusMinutes(5);
        LoginCaptchaBO captchaBO = new LoginCaptchaBO(captcha, time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String json = objectMapper.writeValueAsString(captchaBO);
        return aesEncode(secret, iv, json);
    }

    protected LoginCaptchaBO decodeTag(String secret, String iv, String tag) throws JsonProcessingException, GeneralSecurityException {
        String decrypted = aesDecode(secret, iv, tag);
        return objectMapper.readValue(decrypted, LoginCaptchaBO.class);
    }

    /**
     * AES加密
     */
    protected String aesEncode(String secret, String iv, String content) throws GeneralSecurityException {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.US_ASCII)));
        byte[] byteEncode = content.getBytes(StandardCharsets.UTF_8);
        // 根据密码器的初始化方式加密
        byte[] byteAES = cipher.doFinal(byteEncode);

        // 将加密后的数据转换为字符串
        return Base64.getEncoder().encodeToString(byteAES);
    }

    /**
     * AES解密
     */
    protected String aesDecode(String secret, String iv, String content) throws GeneralSecurityException {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.US_ASCII)));
        // 将加密并编码后的内容解码成字节数组
        byte[] byteContent = Base64.getDecoder().decode(content);
        // 解密
        byte[] byteDecode = cipher.doFinal(byteContent);
        return new String(byteDecode, StandardCharsets.UTF_8);
    }

    /**
     * 颜色的设置
     */
    protected Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + RANDOM.nextInt(bc - fc - 16);
        int g = fc + RANDOM.nextInt(bc - fc - 14);
        int b = fc + RANDOM.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 随机字符的获取
     */
    public String getRandomString(int num) {
        num = num > 0 ? num : RANDOM_STRING.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            int number = RANDOM.nextInt(RANDOM_STRING.length());
            sb.append(RANDOM_STRING.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 干扰线的绘制
     */
    protected void drawLine(Graphics2D g) {
        int x = RANDOM.nextInt(WIDTH);
        int y = RANDOM.nextInt(HEIGHT);
        int xl = WIDTH;
        int yl = HEIGHT;
        g.setStroke(new BasicStroke(2.0f));
        g.setColor(getRandomColor(98, 200));
        g.drawLine(x, y, x + xl, y + yl);
    }

    /**
     * 字符串的绘制
     */
    protected void drawString(Graphics2D g, String randomStr, int i) throws IOException, FontFormatException {
        g.setFont(getFont());
        g.setColor(getRandomColor(28, 130));
        // 设置每个字符的随机旋转
        double radianPercent = (RANDOM.nextBoolean() ? -1 : 1) * Math.PI * (RANDOM.nextInt(60) / 320D);
        g.rotate(radianPercent, WIDTH * 0.8 / RANDOM_STR_NUM * i, HEIGHT / 2);
        int y = (RANDOM.nextBoolean() ? -1 : 1) * RANDOM.nextInt(4) + 4;
        g.translate(RANDOM.nextInt(3), y);
        g.drawString(randomStr, WIDTH / RANDOM_STR_NUM * i, HEIGHT / 2);
        g.rotate(-radianPercent, WIDTH * 0.8 / RANDOM_STR_NUM * i, HEIGHT / 2);
        g.translate(0, -y);
    }

    protected BufferedImage getBufferedImage(String code) throws IOException, FontFormatException {
        // BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(getRandomColor(105, 189));
        g.setFont(getFont());
        int lineSize = RANDOM.nextInt(5);
        // 干扰线
        for (int i = 0; i < lineSize; i++) {
            drawLine(g);
        }
        // 随机字符
        for (int i = 0; i < code.length(); i++) {
            drawString(g, String.valueOf(code.charAt(i)), i);
        }
        g.dispose();
        return image;
    }

    /**
     * 生成随机图片的base64编码字符串
     *
     * @param code 验证码
     * @return base64
     */
    protected String getRandomCodeBase64(String code) throws IOException, FontFormatException {
        BufferedImage image = getBufferedImage(code);
        // 返回 base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        byte[] bytes = bos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes);
    }
}
