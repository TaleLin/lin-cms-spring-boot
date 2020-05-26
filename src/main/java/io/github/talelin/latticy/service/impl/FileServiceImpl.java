package io.github.talelin.latticy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.module.file.FileConstant;
import io.github.talelin.latticy.module.file.FileProperties;
import io.github.talelin.latticy.module.file.Uploader;
import io.github.talelin.latticy.mapper.FileMapper;
import io.github.talelin.latticy.model.FileDO;
import io.github.talelin.latticy.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Autowired
    private Uploader uploader;

    /**
     * 文件上传配置信息
     */
    @Autowired
    private FileProperties fileProperties;

    /**
     * 为什么不做批量插入
     * 1. 文件上传的数量一般不多，3个左右
     * 2. 批量插入不能得到数据的id字段，不利于直接返回数据
     * 3. 批量插入也仅仅只是一条sql语句的事情，如果真的需要，可以自行尝试一下
     */
    @Override
    public List<FileBO> upload(MultiValueMap<String, MultipartFile> fileMap) {
        List<FileBO> res = new ArrayList<>();
        uploader.upload(fileMap, file -> {
            FileDO found = this.baseMapper.selectByMd5(file.getMd5());
            // 数据库中不存在
            if (found == null) {
                FileDO fileDO = new FileDO();
                BeanUtil.copyProperties(file, fileDO);
                this.getBaseMapper().insert(fileDO);
                res.add(transformDoToBo(fileDO, file.getKey()));
                return true;
            }
            // 已存在，则直接转化返回
            res.add(transformDoToBo(found, file.getKey()));
            return false;
        });
        return res;
    }

    @Override
    public boolean checkFileExistByMd5(String md5) {
        return this.getBaseMapper().selectCountByMd5(md5) > 0;
    }

    private FileBO transformDoToBo(FileDO file, String key) {
        FileBO bo = new FileBO();
        BeanUtil.copyProperties(file, bo);
        if (file.getType().equals(FileConstant.LOCAL)) {
            String s = fileProperties.getServePath().split("/")[0];

            // replaceAll 是将 windows 平台下的 \ 替换为 /
            if(System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS")){
                bo.setUrl(fileProperties.getDomain() + s + "/" + file.getPath().replaceAll("\\\\","/"));
            }else {
                bo.setUrl(fileProperties.getDomain() + s + "/" + file.getPath());
            }
        } else {
            bo.setUrl(file.getPath());
        }
        bo.setKey(key);
        return bo;
    }
}
