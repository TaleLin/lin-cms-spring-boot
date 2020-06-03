package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.mapper.LogMapper;
import io.github.talelin.latticy.model.LogDO;
import io.github.talelin.latticy.service.LogService;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class LogServiceImplTest {

    @Autowired
    private LogService logService;

    @Autowired
    private LogMapper logMapper;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getLogs() {
        String message = "put your face to the light!";
        String authority = "查看lin的信息";
        Integer userId = 100;
        String userName = "pedro";
        String method = "GET";
        String path = "/";
        Integer status = 200;
        logService.createLog(message, authority, userId, userName, method, path, status);

        IPage<LogDO> iPage = logService.getLogPage(0, 10, null, null, null);
        assertTrue(iPage.getSize() > 0);
    }

    @Test
    public void searchLogs() {
        String message = "put your face to the light!";
        String authority = "查看lin的信息";
        Integer userId = 100;
        String userName = "pedro";
        String method = "GET";
        String path = "/";
        Integer status = 200;
        logService.createLog(message, authority, userId, userName, method, path, status);

        IPage<LogDO> iPage = logService.searchLogPage(0, 10, null, "put", null, null);
        assertTrue(iPage.getSize() > 0);
    }

    @Test
    public void getUserNames() {
        String message = "put your face to the light!";
        String authority = "查看lin的信息";
        Integer userId = 100;
        String userName = "pedro";
        String method = "GET";
        String path = "/";
        Integer status = 200;
        logService.createLog(message, authority, userId, userName, method, path, status);

        IPage<String> iPage = logService.getUserNamePage(0, 10);
        assertTrue(iPage.getRecords().size() > 0);
    }

    @Test
    public void createOneLog() {
        String message = "put your face to the light!";
        String permission = "查看lin的信息";
        Integer userId = 100;
        String userName = "pedro";
        String method = "GET";
        String path = "/";
        Integer status = 200;
        logService.createLog(message, permission, userId, userName, method, path, status);

        QueryWrapper<LogDO> condition = new QueryWrapper<>();
        condition.eq("message", message);
        LogDO logDO = logMapper.selectOne(condition);

        assertEquals(logDO.getPermission(), permission);
    }
}