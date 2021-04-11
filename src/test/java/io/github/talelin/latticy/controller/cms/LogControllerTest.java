package io.github.talelin.latticy.controller.cms;

import io.github.talelin.latticy.mapper.LogMapper;
import io.github.talelin.latticy.model.LogDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LogControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LogMapper logMapper;

    @BeforeAll
    public void setUp() throws Exception {
        Date time = new Date();
        String permission = "查看lin的信息";
        String message = "就是个瓜皮";
        String method = "GET";
        String path = "/";
        Integer statusCode = 200;
        Integer userId = 1;
        String username = "pedro大大";

        LogDO logDO = LogDO
                .builder()
                .permission(permission)
                .message(message)
                .method(method)
                .statusCode(statusCode)
                .path(path)
                .userId(userId)
                .username(username)
                .build();
        logMapper.insert(logDO);
    }

    @Test
    public void getLogs() throws Exception {
        mvc.perform(get("/cms/log/").param("name", "pedro大大")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").isNumber());

    }

    @Test
    public void getLogs1() throws Exception {
        mvc.perform(get("/cms/log/").param("name", "pedro")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isEmpty());

    }

    @Test
    public void getLogs2() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String yesterday = LocalDateTime.now().plusDays(-1).format(dateTimeFormatter);
        String tomorrow = LocalDateTime.now().plusDays(1).format(dateTimeFormatter);
        mvc.perform(get("/cms/log/")
                .param("name", "pedro")
                .param("start", yesterday)
                .param("end", tomorrow)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isEmpty());

    }

    @Test
    public void getLogs3() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String yesterday = LocalDateTime.now().plusDays(-1).format(dateTimeFormatter);
        String tomorrow = LocalDateTime.now().plusDays(1).format(dateTimeFormatter);
        mvc.perform(get("/cms/log/")
                .param("name", "pedro")
                .param("start", yesterday)
                .param("end", tomorrow)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isEmpty());

    }

    @Test
    public void searchLogs() throws Exception {
        mvc.perform(get("/cms/log/search")
                .param("name", "pedro大大")
                .param("keyword", "瓜皮")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray());
    }

    @Test
    public void searchLogs1() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String yesterday = LocalDateTime.now().plusDays(-1).format(dateTimeFormatter);
        String tomorrow = LocalDateTime.now().plusDays(1).format(dateTimeFormatter);
        log.info("{}, {}", yesterday, tomorrow);
        mvc.perform(get("/cms/log/search")
                .param("start", yesterday)
                .param("end", tomorrow)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray());
    }

    @Test
    public void getUsers() throws Exception {
        mvc.perform(get("/cms/log/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.items").isArray());
    }
}