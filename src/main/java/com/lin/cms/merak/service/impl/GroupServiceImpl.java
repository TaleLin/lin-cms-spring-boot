package com.lin.cms.merak.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lin.cms.merak.bo.GroupPermissionsBO;
import com.lin.cms.merak.common.mybatis.Page;
import com.lin.cms.merak.mapper.UserGroupMapper;
import com.lin.cms.merak.model.GroupDO;
import com.lin.cms.merak.mapper.GroupMapper;
import com.lin.cms.merak.model.PermissionDO;
import com.lin.cms.merak.model.UserGroupDO;
import com.lin.cms.merak.service.GroupService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lin.cms.merak.service.PermissionService;
import com.lin.cms.autoconfigure.exception.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author pedro
 * @since 2019-11-30
 */
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    @Value("${group.root.name}")
    private String rootGroupName;

    @Value("${group.root.id}")
    private Long rootGroupId;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Override
    public List<GroupDO> getUserGroupsByUserId(Long userId) {
        return this.baseMapper.selectUserGroups(userId);
    }

    @Override
    public List<Long> getUserGroupIdsByUserId(Long userId) {
        return this.baseMapper.selectUserGroupIds(userId);
    }

    @Override
    public IPage<GroupDO> getGroupPage(long page, long count) {
        Page pager = new Page(page, count);
        return this.baseMapper.selectPage(pager, null);
    }

    @Override
    public boolean checkGroupExistById(Long id) {
        return this.baseMapper.selectCountById(id) > 0;
    }

    @Override
    public GroupPermissionsBO getGroupAndPermissions(Long id) {
        GroupDO group = this.baseMapper.selectById(id);
        List<PermissionDO> permissions = permissionService.getPermissionByGroupId(id);
        return new GroupPermissionsBO(group, permissions);
    }

    @Override
    public boolean checkGroupExistByName(String name) {
        QueryWrapper<GroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(GroupDO::getName, name);
        return this.baseMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean checkIsRootByUserId(Long userId) {
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserGroupDO::getUserId, userId)
                .eq(UserGroupDO::getGroupId, rootGroupId);
        UserGroupDO relation = userGroupMapper.selectOne(wrapper);
        return relation != null;
    }

    @Override
    public boolean deleteUserGroupRelations(Long userId, List<Long> deleteIds) {
        if (deleteIds == null || deleteIds.isEmpty())
            return true;
        QueryWrapper<UserGroupDO> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(UserGroupDO::getUserId, userId)
                .in(UserGroupDO::getGroupId, deleteIds);
        return userGroupMapper.delete(wrapper) > 0;
    }

    @Override
    public boolean addUserGroupRelations(Long userId, List<Long> addIds) {
        if (addIds == null || addIds.isEmpty())
            return true;
        boolean ok = checkGroupExistByIds(addIds);
        if (!ok) {
            throw new ForbiddenException("cant't add user to non-existent group", 10077);
        }
        List<UserGroupDO> relations = addIds.stream().map(it -> new UserGroupDO(userId, it)).collect(Collectors.toList());
        return userGroupMapper.insertBatch(relations) > 0;
    }

    private boolean checkGroupExistByIds(List<Long> ids) {
        return ids.stream().allMatch(this::checkGroupExistById);
    }
}
