package io.github.talelin.latticy.controller.cms;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
public class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    /**
     * 由于oss账户信息涉及隐私，所以代码中没有提供，
     * 开启测试前需要提供对应的配置，具体配置参见
     * {@link io.github.talelin.latticy.extension.file.AliyunOSSUploader}
     * 中有 @Value 注解的属性
     */
//    @Test
    public void upload() throws Exception {

        String filename = "test-upload.png";
        File file = ResourceUtils.getFile("classpath:" + filename);

        MockMultipartFile uploadFile = new MockMultipartFile(
                "file", filename,
                MediaType.TEXT_PLAIN_VALUE, new FileInputStream(file));

        mvc.perform(multipart("/cms/file").file(uploadFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]['id']").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]['path']").isString());
    }
}