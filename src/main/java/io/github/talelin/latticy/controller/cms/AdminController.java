package io.github.talelin.latticy.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.AdminMeta;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.util.ResponseUtil;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.AdminService;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.vo.*;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.dto.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pedro@TaleLin
 */
@RestController
@RequestMapping("/cms/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private GroupService groupService;

    @GetMapping("/permission")
    @AdminMeta(permission = "查询所有可分配的权限", module = "管理员")
    public Map<String, List<PermissionDO>> getAllPermissions() {
        return adminService.getAllStructualPermissions();
    }


    @GetMapping("/users")
    @AdminMeta(permission = "查询所有用户", module = "管理员")
    public PageResponseVO getUsers(
            @RequestParam(name = "group_id", required = false)
            @Min(value = 1, message = "{group-id}") Long groupId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<UserDO> iPage = adminService.getUserPageByGroupId(groupId, count, page);
        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
            List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
            return new UserInfoVO(user, groups);
        }).collect(Collectors.toList());
        return ResponseUtil.generatePageResult(iPage.getTotal(), userInfos, page, count);
    }

    @PutMapping("/user/{id}/password")
    @AdminMeta(permission = "修改用户密码", module = "管理员")
    public UpdatedVO changeUserPassword(@PathVariable @Positive(message = "{id}") Long id, @RequestBody @Validated ResetPasswordDTO validator) {
        adminService.changeUserPassword(id, validator);
        return new UpdatedVO(4);
    }

    @DeleteMapping("/user/{id}")
    @AdminMeta(permission = "删除用户", module = "管理员")
    public DeletedVO deleteUser(@PathVariable @Positive(message = "{id}") Long id) {
        adminService.deleteUser(id);
        return new DeletedVO(5);
    }

    @PutMapping("/user/{id}")
    @AdminMeta(permission = "管理员更新用户信息", module = "管理员")
    public UpdatedVO updateUser(@PathVariable @Positive(message = "{id}") Long id, @RequestBody @Validated UpdateUserInfoDTO validator) {
        adminService.updateUserInfo(id, validator);
        return new UpdatedVO(6);
    }

    @GetMapping("/group")
    @AdminMeta(permission = "查询所有权限组及其权限", module = "管理员")
    public PageResponseVO getGroups(
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{count}") Long count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page}") Long page) {
        IPage<GroupDO> iPage = adminService.getGroupPage(page, count);
        return ResponseUtil.generatePageResult(iPage.getTotal(), iPage.getRecords(), page, count);
    }

    @GetMapping("/group/all")
    @AdminMeta(permission = "查询所有权限组", module = "管理员")
    public List<GroupDO> getAllGroup() {
        List<GroupDO> groups = adminService.getAllGroups();
        return groups;
    }

    @GetMapping("/group/{id}")
    @AdminMeta(permission = "查询一个权限组及其权限", module = "管理员")
    public GroupPermissionBO getGroup(@PathVariable @Positive(message = "{id}") Long id) {
        GroupPermissionBO groupPermissions = adminService.getGroup(id);
        return groupPermissions;
    }

    @PostMapping("/group")
    @AdminMeta(permission = "新建权限组", module = "管理员")
    public CreatedVO createGroup(@RequestBody @Validated NewGroupDTO validator) {
        adminService.createGroup(validator);
        return new CreatedVO(15);
    }

    @PutMapping("/group/{id}")
    @AdminMeta(permission = "更新一个权限组", module = "管理员")
    public UpdatedVO updateGroup(@PathVariable @Positive(message = "{id}") Long id,
                                       @RequestBody @Validated UpdateGroupDTO validator) {
        adminService.updateGroup(id, validator);
        return new UpdatedVO(7);
    }

    @DeleteMapping("/group/{id}")
    @AdminMeta(permission = "删除一个权限组", module = "管理员")
    public DeletedVO deleteGroup(@PathVariable @Positive(message = "{id}") Long id) {
        adminService.deleteGroup(id);
        return new DeletedVO(8);
    }

    @PostMapping("/permission/dispatch")
    @AdminMeta(permission = "分配单个权限", module = "管理员")
    public CreatedVO dispatchPermission(@RequestBody @Validated DispatchPermissionDTO validator) {
        adminService.dispatchPermission(validator);
        return new CreatedVO(9);
    }

    @PostMapping("/permission/dispatch/batch")
    @AdminMeta(permission = "分配多个权限", module = "管理员")
    public CreatedVO dispatchPermissions(@RequestBody @Validated DispatchPermissionsDTO validator) {
        adminService.dispatchPermissions(validator);
        return new CreatedVO(9);
    }

    @PostMapping("/permission/remove")
    @AdminMeta(permission = "删除多个权限", module = "管理员")
    public DeletedVO removePermissions(@RequestBody @Validated RemovePermissionsDTO validator) {
        adminService.removePermissions(validator);
        return new DeletedVO(10);
    }

}
