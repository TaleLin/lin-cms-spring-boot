package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.common.mybatis.Page;
import io.github.talelin.latticy.model.LogDO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class LogMapperTest {


    @Autowired
    private LogMapper logMapper;

    private Date start = new Date();
    private String permission = "查看lin的信息";
    private String message = "就是个瓜皮";
    private String method = "GET";
    private String path = "/";
    private Integer statusCode = 200;
    private Integer userId = 1;
    private String username = "super";

    @Before
    public void setUp() throws Exception {
        LogDO logDO = new LogDO();
        logDO.setPermission(permission);
        logDO.setMessage(message);
        logDO.setMethod(method);
        logDO.setPath(path);
        logDO.setStatusCode(statusCode);
        logDO.setUserId(userId);
        logDO.setUsername(username);
        logDO.setCreateTime(start);
        long ll = start.getTime() - 500000;
        // start.setTime(ll);
        start = new Date(ll);
        logMapper.insert(logDO);
    }

    @Test
    public void testFindLogsByUsernameAndRange() {
        Date now = new Date();
        Page page = new Page(0, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, start, now);
        List<LogDO> logs = iPage.getRecords();
        assertTrue(logs.size() > 0);
    }

    @Test
    public void testFindLogsByUsernameAndRange1() {
        long changed = start.getTime();
        Date ch = new Date(changed - 1000);
        Date ch1 = new Date(changed - 2000);
        Page page = new Page(1, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, ch1, ch);
        List<LogDO> logs = iPage.getRecords();
        assertTrue(logs.size() == 0);
    }

    @After
    public void tearDown() throws Exception {
    }
}