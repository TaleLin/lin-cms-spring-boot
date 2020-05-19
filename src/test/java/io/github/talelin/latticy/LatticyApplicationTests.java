package io.github.talelin.latticy;

import io.github.talelin.latticy.module.file.FileProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LatticyApplicationTests {

    @Autowired
    private FileProperties fileProperties;

    @Test
    public void contextLoads() {
        System.out.println();
    }

}
