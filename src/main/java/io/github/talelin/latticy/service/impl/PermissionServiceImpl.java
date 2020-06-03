package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
    public Map<Long, List<PermissionDO>> getPermissionMapByGroupIds(List<Integer> groupIds) {
        HashMap map = new HashMap(groupIds.size());
        groupIds.stream().forEach(groupId -> {
            List<PermissionDO> permissions = this.baseMapper.selectPermissionsByGroupId(groupId);
            map.put(groupId, permissions);
        });
        return map;
    }

    @Override
    public List<Map<String, List<Map<String, String>>>> structuringPermissions(List<PermissionDO> permissions) {
        Map<String, List<Map<String, String>>> tmp = new HashMap();
        permissions.forEach(permission -> {
            if (!tmp.containsKey(permission.getModule())) {
                Map<String, String> tiny = new HashMap();
                tiny.put("module", permission.getModule());
                tiny.put("permission", permission.getName());
                List<Map<String, String>> mini = new ArrayList();
                mini.add(tiny);
                tmp.put(permission.getModule(), mini);
            } else {
                Map<String, String> tiny = new HashMap();
                tiny.put("module", permission.getModule());
                tiny.put("permission", permission.getName());
                tmp.get(permission.getModule()).add(tiny);
            }
        });
        List<Map<String, List<Map<String, String>>>> structualPermissions = new ArrayList();
        tmp.forEach((k, v) -> {
            Map<String, List<Map<String, String>>> ttmp = new HashMap();
            ttmp.put(k, v);
            structualPermissions.add(ttmp);
        });
        return structualPermissions;
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
