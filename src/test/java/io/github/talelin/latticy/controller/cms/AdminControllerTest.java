package io.github.talelin.latticy.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import io.github.talelin.latticy.dto.admin.*;
import io.github.talelin.latticy.mapper.*;
import io.github.talelin.latticy.model.*;
import io.github.talelin.latticy.service.impl.UserIdentityServiceImpl;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Rollback
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
public class AdminControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private UserIdentityServiceImpl userIdentityService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAllPermissions() throws Exception {
        mvc.perform(get("/cms/admin/permission")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.日志").isArray());
    }

    @Test
    public void getUsers() throws Exception {
        mvc.perform(get("/cms/admin/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(10));
    }

    @Test
    public void getUsers1() throws Exception {
        String email = "13129982604@qq.com";
        String username = "pedro大大";
        String nickname = "pedro大大";
        UserDO user = UserDO.builder().username(username).nickname(nickname).email(email).build();
        userMapper.insert(user);


        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));

        mvc.perform(get("/cms/admin/users")
                .param("group_id", group.getId() + "")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray());
    }

    @Test
    public void changeUserPassword() throws Exception {
        String email = "13129982604@qq.com";
        String username = "pedro大大";
        String nickname = "pedro大大";
        UserDO user = UserDO.builder().username(username).nickname(nickname).email(email).build();
        userMapper.insert(user);


        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));
        userIdentityService.createUsernamePasswordIdentity(user.getId(), username, "123456");

        String newPassword = "111111111";

        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setNewPassword(newPassword);
        dto.setConfirmPassword(newPassword);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(put(String.format("/cms/admin/user/%s/password", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("密码修改成功"));

        boolean b = userIdentityService.verifyUsernamePassword(user.getId(), username, newPassword);
        assertTrue(b);
    }

    @Test
    public void deleteUser() throws Exception {
        String email = "13129982604@qq.com";
        String username = "pedro大大";
        String nickname = "pedro大大";
        UserDO user = UserDO.builder().username(username).nickname(nickname).email(email).build();
        userMapper.insert(user);


        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));

        mvc.perform(delete("/cms/admin/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("删除用户成功"));

        UserDO hit = userMapper.selectById(user.getId());
        assertNull(hit);
    }

    @Test
    public void updateUser() throws Exception {
        String email = "13129982604@qq.com";
        String username = "pedro大大";
        String nickname = "pedro大大";
        UserDO user = UserDO.builder().username(username).nickname(nickname).email(email).build();
        userMapper.insert(user);


        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
        dto.setGroupIds(Arrays.asList(group.getId()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(put("/cms/admin/user/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("更新用户成功"));
    }

    @Test
    public void getGroups() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));

        mvc.perform(get("/cms/admin/group")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.items").isArray());
    }

    @Test
    public void getAllGroup() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));

        mvc.perform(get("/cms/admin/group/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
    }

    @Test
    public void getGroup() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));

        mvc.perform(get("/cms/admin/group/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    @Test
    public void createGroup() throws Exception {
        NewGroupDTO dto = new NewGroupDTO();
        dto.setName("flink");
        dto.setInfo("flink is a finger");

        String module = "信息";
        String permissionName = "查看lin的信息";
        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        dto.setPermissionIds(Arrays.asList(permission.getId()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/admin/group/")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("新建分组成功"));
    }

    @Test
    public void updateGroup() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));

        UpdateGroupDTO dto = new UpdateGroupDTO();
        dto.setName("storm");
        dto.setInfo("flink is a finger");

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(put("/cms/admin/group/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("更新分组成功"));

        GroupDO hit = groupMapper.selectById(group.getId());
        assertEquals(hit.getName(), "storm");
    }

    @Test
    public void deleteGroup() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO root = GroupDO.builder().name("root").info(info).build();
        GroupDO guest = GroupDO.builder().name("guest").info(info).build();
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(root);
        groupMapper.insert(guest);
        groupMapper.insert(group);

        mvc.perform(delete("/cms/admin/group/" + group.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("删除分组成功"));
        GroupDO hit = groupMapper.selectById(group.getId());
        assertNull(hit);
    }

    @Test
    public void dispatchPermission() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";

        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);

        DispatchPermissionDTO dto = new DispatchPermissionDTO();
        dto.setGroupId(group.getId());
        dto.setPermissionId(permission.getId());

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/admin/permission/dispatch")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("添加权限成功"));
    }

    @Test
    public void dispatchPermissions() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";
        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);
        String permissionName1 = "查看pedro信息";
        PermissionDO permission1 = PermissionDO.builder().name(permissionName1).module(module).build();
        permissionMapper.insert(permission1);

        DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
        dto.setGroupId(group.getId());
        dto.setPermissionIds(Arrays.asList(permission.getId(), permission1.getId()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/admin/permission/dispatch/batch")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("添加权限成功"));
    }

    @Test
    public void removePermissions() throws Exception {
        String name = "千里之外";
        String info = "千里之外是个啥";
        GroupDO group = GroupDO.builder().name(name).info(info).build();
        groupMapper.insert(group);

        String module = "信息";
        String permissionName = "查看lin的信息";
        PermissionDO permission = PermissionDO.builder().name(permissionName).module(module).build();
        permissionMapper.insert(permission);
        String permissionName1 = "查看pedro信息";
        PermissionDO permission1 = PermissionDO.builder().name(permissionName1).module(module).build();
        permissionMapper.insert(permission1);

        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission.getId()));
        groupPermissionMapper.insert(new GroupPermissionDO(group.getId(), permission1.getId()));

        RemovePermissionsDTO dto = new RemovePermissionsDTO();
        dto.setGroupId(group.getId());
        dto.setPermissionIds(Arrays.asList(permission1.getId()));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        String content = mapper.writeValueAsString(dto);

        mvc.perform(post("/cms/admin/permission/remove")
                .contentType(MediaType.APPLICATION_JSON).content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("删除权限成功"));
    }
}