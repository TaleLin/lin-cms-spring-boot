package io.github.talelin.latticy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.model.GroupDO;

import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 获得用户的所有分组
     *
     * @param userId 用户id
     * @return 所有分组
     */
    List<GroupDO> getUserGroupsByUserId(Integer userId);

    /**
     * 获得用户的所有分组id
     *
     * @param userId 用户id
     * @return 所有分组id
     */
    List<Integer> getUserGroupIdsByUserId(Integer userId);

    /**
     * 分页获取分组数据
     *
     * @param count 分页数量
     * @param page  那一页
     * @return 分组页
     */
    IPage<GroupDO> getGroupPage(int page, int count);

    /**
     * 通过id检查分组是否存在
     *
     * @param id 分组id
     * @return 是否存在
     */
    boolean checkGroupExistById(Integer id);

    /**
     * 获得分组及其权限
     *
     * @param id 分组id
     * @return 分组及权限
     */
    GroupPermissionBO getGroupAndPermissions(Integer id);

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
    boolean checkIsRootByUserId(Integer userId);

    /**
     * 删除用户与分组直接的关联
     *
     * @param userId    用户id
     * @param deleteIds 分组id
     */
    boolean deleteUserGroupRelations(Integer userId, List<Integer> deleteIds);

    /**
     * 添加用户与分组直接的关联
     *
     * @param userId 用户id
     * @param addIds 分组id
     */
    boolean addUserGroupRelations(Integer userId, List<Integer> addIds);

    /**
     * 获得分组下所有用户的id
     *
     * @param id 分组id
     * @return 用户id
     */
    List<Integer> getGroupUserIds(Integer id);

    /**
     * 通过分组级别获取超级管理员分组或游客分组
     *
     * @param level GroupLevelEnum 枚举类
     * @return 用户组
     */
    GroupDO getParticularGroupByLevel(GroupLevelEnum level);

    /**
     * 通过分组级别获取超级管理员分组或游客分组的id
     *
     * @param level GroupLevelEnum 枚举类
     * @return 用户组id
     */
    Integer getParticularGroupIdByLevel(GroupLevelEnum level);
}
