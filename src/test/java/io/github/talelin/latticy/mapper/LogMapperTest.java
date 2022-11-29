package io.github.talelin.latticy.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.model.LogDO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LogMapperTest {

    @Autowired
    private LogMapper logMapper;

    private Date start = new Date();
    private final String permission = "查看lin的信息";
    private final String message = "就是个瓜皮";
    private final String method = "GET";
    private final String path = "/";
    private final Integer statusCode = 200;
    private final Integer userId = 1;
    private final String username = "super";

    @BeforeAll
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
    void testFindLogsByUsernameAndRange() {
        Date now = new Date();
        LinPage<LogDO> page = new LinPage<>(0, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, start, now);
        List<LogDO> logs = iPage.getRecords();
        assertTrue(logs.size() > 0);
    }

    @Test
    void testFindLogsByUsernameAndRange1() {
        long changed = start.getTime();
        Date ch = new Date(changed - 1000);
        Date ch1 = new Date(changed - 2000);
        LinPage<LogDO> page = new LinPage<>(1, 10);
        IPage<LogDO> iPage = logMapper.findLogsByUsernameAndRange(page, username, ch1, ch);
        List<LogDO> logs = iPage.getRecords();
        assertEquals(0, logs.size());
    }

}