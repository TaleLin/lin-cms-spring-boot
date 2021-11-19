package io.github.talelin.latticy.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.talelin.latticy.bo.LoginCaptchaBO;
import org.springframework.core.io.ClassPathResource;

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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Random;

/**
 * @author Gadfly
 */
@SuppressWarnings("SpellCheckingInspection")
public class CaptchaUtil {

    /**
     * 验证码字符个数
     */
    public static final int RANDOM_STR_NUM = 4;
    private static final Random RANDOM = new Random();
    /**
     * 验证码的宽
     */
    private static final int WIDTH = 80;
    /**
     * 验证码的高
     */
    private static final int HEIGHT = 40;
    private static final String RANDOM_STRING = "23456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWSYZ";
    private static final String AES = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        java.security.Security.setProperty("crypto.policy", "unlimited");
    }

    /**
     * 颜色的设置
     */
    private static Color getRandomColor(int fc, int bc) {

        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);

        int r = fc + RANDOM.nextInt(bc - fc - 16);
        int g = fc + RANDOM.nextInt(bc - fc - 14);
        int b = fc + RANDOM.nextInt(bc - fc - 12);

        return new Color(r, g, b);
    }

    /**
     * 字体的设置
     */
    private static Font getFont() throws IOException, FontFormatException {
        ClassPathResource dejavuSerifBold = new ClassPathResource("DejaVuSerif-Bold.ttf");
        return Font.createFont(Font.TRUETYPE_FONT, dejavuSerifBold.getInputStream()).deriveFont(Font.BOLD, 24);
    }

    /**
     * 随机字符的获取
     */
    public static String getRandomString(int num) {
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
    private static void drawLine(Graphics2D g) {
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
    private static void drawString(Graphics2D g, String randomStr, int i) throws IOException, FontFormatException {
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

    private static BufferedImage getBufferedImage(String code) throws IOException, FontFormatException {
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
    public static String getRandomCodeBase64(String code) throws IOException, FontFormatException {
        BufferedImage image = getBufferedImage(code);
        // 返回 base64
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);

        byte[] bytes = bos.toByteArray();
        Base64.Encoder encoder = Base64.getEncoder();

        return encoder.encodeToString(bytes);
    }

    public static String getTag(String captcha, String secret, String iv) throws JsonProcessingException, GeneralSecurityException {
        LocalDateTime time = LocalDateTime.now().plusMinutes(5);
        LoginCaptchaBO captchaBO = new LoginCaptchaBO(captcha, time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String json = MAPPER.writeValueAsString(captchaBO);
        return aesEncode(secret, iv, json);
    }

    public static LoginCaptchaBO decodeTag(String secret, String iv, String tag) throws JsonProcessingException, GeneralSecurityException {
        String decrypted = aesDecode(secret, iv, tag);
        return MAPPER.readValue(decrypted, LoginCaptchaBO.class);
    }

    /**
     * AES加密
     */
    public static String aesEncode(String secret, String iv, String content) throws GeneralSecurityException {
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
    public static String aesDecode(String secret, String iv, String content) throws GeneralSecurityException {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), AES);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv.getBytes(StandardCharsets.US_ASCII)));
        // 将加密并编码后的内容解码成字节数组
        byte[] byteContent = Base64.getDecoder().decode(content);
        // 解密
        byte[] byteDecode = cipher.doFinal(byteContent);
        return new String(byteDecode, StandardCharsets.UTF_8);
    }
}