package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.talelin.latticy.bo.ModulePermissionBO;
import io.github.talelin.latticy.mapper.PermissionMapper;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, PermissionDO> implements PermissionService {


    @Override
    public List<PermissionDO> getPermissionByGroupId(Integer groupId) {
        return this.baseMapper.selectPermissionsByGroupId(groupId);
    }

    @Override
    public List<PermissionDO> getPermissionByGroupIds(List<Integer> groupIds) {
        return this.baseMapper.selectPermissionsByGroupIds(groupIds);
    }

    /**
     * 为什么不使用联表进行查询？
     * 1. 联表很麻烦，需要关联2，3次，涉及到3张表，会严重影响性能
     * 2. 由于使用了IN关键字，所以性能其实很不好
     * 3. 不直观，可读性差
     * 4. 用户的分组一般都比较少，一般情况下都在2个一下
     */
    @Override
    public Map<Integer, List<PermissionDO>> getPermissionMapByGroupIds(List<Integer> groupIds) {
        HashMap<Integer, List<PermissionDO>> map = new HashMap<>(groupIds.size());
        groupIds.forEach(groupId -> {
            List<PermissionDO> permissions = this.baseMapper.selectPermissionsByGroupId(groupId);
            map.put(groupId, permissions);
        });
        return map;
    }

    @Override
    public List<Map<String, List<ModulePermissionBO>>> structuringPermissions(List<PermissionDO> permissions) {
        Map<String, List<ModulePermissionBO>> tmp = new HashMap<>();
        permissions.forEach(permission -> {
            ModulePermissionBO tiny = new ModulePermissionBO(permission.getModule(), permission.getName());
            if (!tmp.containsKey(permission.getModule())) {
                List<ModulePermissionBO> mini = new ArrayList<>();
                mini.add(tiny);
                tmp.put(permission.getModule(), mini);
            } else {
                tmp.get(permission.getModule()).add(tiny);
            }
        });
        List<Map<String, List<ModulePermissionBO>>> structuralPermissions = new ArrayList<>();
        tmp.forEach((k, v) -> {
            Map<String, List<ModulePermissionBO>> temp2 = new HashMap<>();
            temp2.put(k, v);
            structuralPermissions.add(temp2);
        });
        return structuralPermissions;
    }

    @Override
    public Map<String, List<String>> structuringPermissionsSimply(List<PermissionDO> permissions) {
        // mod      permission.names
        Map<String, List<String>> res = new HashMap<>();
        permissions.forEach(permission -> {
            if (res.containsKey(permission.getModule())) {
                List<String> mod = res.get(permission.getModule());
                mod.add(permission.getName());
            } else {
                List<String> mod = new ArrayList<>();
                mod.add(permission.getName());
                res.put(permission.getModule(), mod);
            }
        });
        return res;
    }

    @Override
    public List<PermissionDO> getPermissionByGroupIdsAndModule(List<Integer> groupIds, String module) {
        return this.baseMapper.selectPermissionsByGroupIdsAndModule(groupIds, module);
    }
}
