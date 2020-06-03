package io.github.talelin.latticy.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.LoginDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.mapper.GroupMapper;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserService userService;

    private String email = "13129982604@qq.com";

    private String password = "123456";

    private String username = "pedro大大";

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void register() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);

        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/user/register")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("注册成功"));
    }

    @Test
    public void register1() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);
        Random random = new Random();
        int rand = random.nextInt();
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId(), rand));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/user/register")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("分组不存在，无法新建用户"));
    }

    @Test
    public void login() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        LoginDTO dto1 = new LoginDTO();
        dto1.setUsername(username);
        dto1.setPassword(password);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto1);

        mvc.perform(post("/cms/user/login")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").isNotEmpty());
    }

    @Test
    public void update() throws Exception {
        GroupDO root = GroupDO.builder().name("root").info("致敬周星星").build();
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(root);
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        UserDO user = userService.getUserByUsername(username);
        LocalUser.setLocalUser(user);

        UpdateInfoDTO dto1 = new UpdateInfoDTO();
        dto1.setEmail("23129982604@qq.com");
        dto1.setUsername("pedro小小");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto1);


        mvc.perform(MockMvcRequestBuilders.put("/cms/user/")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("更新用户成功"));

        UserDO user1 = userService.getUserByUsername("pedro小小");
        assertTrue(user1.getEmail().equals("23129982604@qq.com"));
    }

    @Test
    public void updatePassword() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        UserDO user = userService.getUserByUsername(username);
        LocalUser.setLocalUser(user);

        ChangePasswordDTO dto1 = new ChangePasswordDTO();
        dto1.setOldPassword(password);
        dto1.setNewPassword("147258");
        dto1.setConfirmPassword("147258");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto1);


        mvc.perform(MockMvcRequestBuilders.put("/cms/user/change_password")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.
                        jsonPath("$.message").value("密码修改成功"));
    }

    @Test
    public void refreshToken() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        UserDO user = userService.getUserByUsername(username);
        LocalUser.setLocalUser(user);

        mvc.perform(MockMvcRequestBuilders.get("/cms/user/refresh")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.access_token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refresh_token").isNotEmpty());
    }

    @Test
    public void getPermissions() throws Exception {
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        UserDO user = userService.getUserByUsername(username);
        LocalUser.setLocalUser(user);

        mvc.perform(MockMvcRequestBuilders.get("/cms/user/permissions")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.permissions").isArray());
    }

    @Test
    public void getInformation() throws Exception {
        GroupDO root = GroupDO.builder().name("少林足球1111111").info("致敬周星星1111111").build();
        GroupDO group = GroupDO.builder().name("少林足球").info("致敬周星星").build();
        groupMapper.insert(root);
        groupMapper.insert(group);
        RegisterDTO dto = new RegisterDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));
        dto.setEmail(email);
        dto.setConfirmPassword(password);
        dto.setPassword(password);
        dto.setUsername(username);
        userService.createUser(dto);

        UserDO user = userService.getUserByUsername(username);
        LocalUser.setLocalUser(user);

        mvc.perform(MockMvcRequestBuilders.get("/cms/user/information")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(username))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.groups").isArray());
    }
}