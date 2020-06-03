package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.model.PermissionDO;

import java.util.List;
import java.util.Map;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public interface PermissionService extends IService<PermissionDO> {

    /**
     * 通过分组id得到分组的权限
     *
     * @param groupId 分组id
     * @return 权限
     */
    List<PermissionDO> getPermissionByGroupId(Integer groupId);

    /**
     * 通过分组id得到分组的权限
     *
     * @param groupIds 分组id
     * @return 权限
     */
    List<PermissionDO> getPermissionByGroupIds(List<Integer> groupIds);

    /**
     * 通过分组id得到分组的权限与分组id的映射
     *
     * @param groupIds 分组id
     * @return 权限map
     */
    Map<Long, List<PermissionDO>> getPermissionMapByGroupIds(List<Integer> groupIds);

    /**
     * 将权限结构化
     *
     * @param permissions 权限
     * @return 结构化的权限
     */
    List<Map<String, List<Map<String, String>>>> structuringPermissions(List<PermissionDO> permissions);

    /**
     * 将权限简单地结构化
     *
     * @param permissions 权限
     * @return 结构化的权限
     */
    Map<String, List<String>> structuringPermissionsSimply(List<PermissionDO> permissions);

    /**
     * 通过分组id和权限模块得到分组的权限与分组id的映射
     *
     * @param groupIds 分组id
     * @param module 权限模块
     * @return 权限map
     */
    List<PermissionDO> getPermissionByGroupIdsAndModule(List<Integer> groupIds, String module);
}
