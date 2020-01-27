package com.lin.cms.merak.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.lin.cms.merak.bo.FileBO;
import com.lin.cms.merak.extensions.file.File;
import com.lin.cms.merak.extensions.file.FileConsts;
import com.lin.cms.merak.extensions.file.Uploader;
import com.lin.cms.merak.mapper.FileMapper;
import com.lin.cms.merak.model.FileDO;
import com.lin.cms.merak.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDO> implements FileService {

    @Autowired
    private Uploader uploader;

    @Value("${lin.cms.file.domain}")
    private String domain;

    @Value("${lin.cms.file.store-dir:assets/}")
    private String dir;

    /**
     * 为什么不做批量插入
     * 1. 文件上传的数量一般不多，3个左右
     * 2. 批量插入不能得到数据的id字段，不利于直接返回数据
     * 3. 批量插入也仅仅只是一条sql语句的事情，如果真的需要，可以自行尝试一下
     */
    @Override
    public List<FileBO> upload(MultiValueMap<String, MultipartFile> fileMap) {
        List<FileDO> tmp = new ArrayList<>();
        List<File> files = uploader.upload(fileMap, file -> {
            FileDO found = this.baseMapper.selectByMd5(file.getMd5());
            if (found == null)
                return true;
            tmp.add(found);
            return false;
        });
        tmp.addAll(files.stream().map(file -> {
            FileDO fileDO = new FileDO();
            BeanUtil.copyProperties(file, fileDO);
            this.getBaseMapper().insert(fileDO);
            return fileDO;
        }).collect(Collectors.toList()));

        List<FileBO> res = tmp.stream().map(file -> {
            FileBO bo = new FileBO();
            BeanUtil.copyProperties(file, bo);
            if (file.getType().equals(FileConsts.LOCAL)) {
                String s = FileUtil.mainName(dir);
                bo.setPath(domain + s + "/" + file.getName());
            }
            return bo;
        }).collect(Collectors.toList());
        return res;
    }

    @Override
    public boolean checkFileExistByMd5(String md5) {
        return this.getBaseMapper().selectCountByMd5(md5) > 0;
    }
}
