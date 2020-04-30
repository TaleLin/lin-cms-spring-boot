package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.model.GroupDO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author pedro@TaleLin
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> getUserGroupsByUserId(Long userId);

    /**
     * 获得用户的所有分组id
     *
     * @param userId 用户id
     * @return 所有分组id
     */
    List<Long> getUserGroupIdsByUserId(Long userId);

    /**
     * 分页获取分组数据
     *
     * @param count 分页数量
     * @param page  那一页
     * @return 分组页
     */
    IPage<GroupDO> getGroupPage(long page, long count);

    /**
     * 通过id检查分组是否存在
     *
     * @param id 分组id
     * @return 是否存在
     */
    boolean checkGroupExistById(Long id);

    /**
     * 获得分组及其权限
     *
     * @param id 分组id
     * @return 分组及权限
     */
    GroupPermissionBO getGroupAndPermissions(Long id);

    /**
     * 通过名称检查分组是否存在
     *
     * @param name 分组名
     * @return 是否存在
     */
    boolean checkGroupExistByName(String name);

    /**
     * 检查该用户是否在root分组中
     *
     * @param userId 用户id
     * @return true表示在
     */
    boolean checkIsRootByUserId(Long userId);

    /**
     * 删除用户与分组直接的关联
     *
     * @param userId    用户id
     * @param deleteIds 分组id
     */
    boolean deleteUserGroupRelations(Long userId, List<Long> deleteIds);

    /**
     * 添加用户与分组直接的关联
     *
     * @param userId 用户id
     * @param addIds 分组id
     */
    boolean addUserGroupRelations(Long userId, List<Long> addIds);

    /**
     * 获得分组下所有用户的id
     *
     * @param id 分组id
     * @return 用户id
     */
    List<Long> getGroupUserIds(Long id);
}
