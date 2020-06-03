package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.mapper.GroupMapper;
import io.github.talelin.latticy.mapper.GroupPermissionMapper;
import io.github.talelin.latticy.mapper.PermissionMapper;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.GroupPermissionDO;
import io.github.talelin.latticy.model.PermissionDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Slf4j
@ActiveProfiles("test")
public class PermissionServiceImplTest {

    @Autowired
    private PermissionServiceImpl permissionService;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private GroupPermissionMapper groupPermissionMapper;

    public Integer mockData() {
        GroupDO group = GroupDO.builder().name("测试分组1").info("just for test").build();
        PermissionDO permission1 = PermissionDO.builder().name("权限1").module("炉石传说").build();
        PermissionDO permission2 = PermissionDO.builder().name("权限2").module("炉石传说").build();
        groupMapper.insert(group);
        permissionMapper.insert(permission1);
        permissionMapper.insert(permission2);
        List<GroupPermissionDO> relations = new ArrayList<>();
        GroupPermissionDO relation1 = new GroupPermissionDO(group.getId(), permission1.getId());
        GroupPermissionDO relation2 = new GroupPermissionDO(group.getId(), permission2.getId());
        relations.add(relation1);
        relations.add(relation2);
        groupPermissionMapper.insertBatch(relations);
        return group.getId();
    }

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getPermissionByGroupId() {
        Integer id = mockData();
        List<PermissionDO> permissions = permissionService.getPermissionByGroupId(id);
        assertTrue(permissions.size() > 0);
        boolean anyMatch = permissions.stream().anyMatch(permission -> permission.getName().equals("权限2"));
        assertTrue(anyMatch);
    }

    @Test
    public void getPermissionByGroupIds() {
        Integer id = mockData();
        List<PermissionDO> permissions = permissionService.getPermissionByGroupIds(Collections.singletonList(id));
        assertTrue(permissions.size() > 0);
        boolean anyMatch = permissions.stream().anyMatch(permission -> permission.getName().equals("权限2"));
        assertTrue(anyMatch);
    }

    @Test
    public void getPermissionMapByGroupIds() {
        Integer id = mockData();
        Map<Long, List<PermissionDO>> map = permissionService.getPermissionMapByGroupIds(Collections.singletonList(id));
        assertNotNull(map.get(id));
        boolean anyMatch = map.get(id).stream().anyMatch(permission -> permission.getName().equals("权限2"));
        assertTrue(anyMatch);
    }

    @Test
    public void structuringPermissions() {
        Integer id = mockData();
        List<PermissionDO> permissions = permissionService.getPermissionByGroupIds(Collections.singletonList(id));
        List<Map<String, List<Map<String, String>>>> structuringPermissions = permissionService.structuringPermissions(permissions);
        assertTrue(structuringPermissions.size() > 0);
        log.info("structuringPermissions: {}", structuringPermissions);
        boolean anyMatch = structuringPermissions.stream().anyMatch(it -> it.containsKey("炉石传说"));
        assertTrue(anyMatch);
    }

    @Test
    public void structuringPermissionsSimply() {
        Integer id = mockData();
        List<PermissionDO> permissions = permissionService.getPermissionByGroupIds(Collections.singletonList(id));
        Map<String, List<String>> structuringPermissions = permissionService.structuringPermissionsSimply(permissions);
        assertTrue(structuringPermissions.size() > 0);
        assertTrue(structuringPermissions.containsKey("炉石传说"));
        log.info("structuringPermissions: {}", structuringPermissions);
    }
}