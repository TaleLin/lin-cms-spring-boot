package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.model.FileDO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FileMapperTest {

    @Autowired
    private FileMapper fileMapper;

    private final String md5 = "iiiiilllllll";
    private final String name = "千里之外";

    @BeforeAll
    public void setUp() throws Exception {
        FileDO fileDO = new FileDO();
        fileDO.setName(name);
        fileDO.setPath("千里之外...");
        fileDO.setSize(1111);
        fileDO.setExtension(".png");
        fileDO.setMd5(md5);
        fileMapper.insert(fileDO);
    }

    @Test
    public void testFindOneByMd5() {
        FileDO one = fileMapper.selectByMd5(md5);
        assertEquals(one.getName(), name);
    }

}