package io.github.talelin.latticy.controller.cms;

import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author pedro@TaleLin
 */
@RestController
@RequestMapping("/cms/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("")
    public List<FileBO> upload(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = ((MultipartHttpServletRequest) request);
        MultiValueMap<String, MultipartFile> fileMap = multipartHttpServletRequest.getMultiFileMap();
        List<FileBO> files = fileService.upload(fileMap);
        return files;
    }

}
