package io.github.talelin.latticy.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.core.annotation.AdminRequired;
import io.github.talelin.core.annotation.PermissionMeta;
import io.github.talelin.core.annotation.PermissionModule;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.util.PageUtil;
import io.github.talelin.latticy.dto.admin.DispatchPermissionDTO;
import io.github.talelin.latticy.dto.admin.DispatchPermissionsDTO;
import io.github.talelin.latticy.dto.admin.NewGroupDTO;
import io.github.talelin.latticy.dto.admin.RemovePermissionsDTO;
import io.github.talelin.latticy.dto.admin.ResetPasswordDTO;
import io.github.talelin.latticy.dto.admin.UpdateGroupDTO;
import io.github.talelin.latticy.dto.admin.UpdateUserInfoDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.AdminService;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.vo.CreatedVO;
import io.github.talelin.latticy.vo.DeletedVO;
import io.github.talelin.latticy.vo.PageResponseVO;
import io.github.talelin.latticy.vo.UpdatedVO;
import io.github.talelin.latticy.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Validated
@RestController
@RequestMapping("/cms/admin")
@PermissionModule(value = "管理员")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private GroupService groupService;

    @AdminRequired
    @GetMapping("/permission")
    @PermissionMeta(value = "查询所有可分配的权限", mount = false)
    public Map<String, List<PermissionDO>> getAllPermissions() {
        return adminService.getAllStructuralPermissions();
    }

    @AdminRequired
    @GetMapping("/users")
    @PermissionMeta(value = "查询所有用户", mount = false)
    public PageResponseVO<UserInfoVO> getUsers(
            @RequestParam(name = "group_id", required = false)
            @Min(value = 1, message = "{group.id.positive}") Integer groupId,
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{page.count.min}")
            @Max(value = 30, message = "{page.count.max}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page.number.min}") Integer page) {
        IPage<UserDO> iPage = adminService.getUserPageByGroupId(groupId, count, page);
        List<UserInfoVO> userInfos = iPage.getRecords().stream().map(user -> {
            List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
            return new UserInfoVO(user, groups);
        }).collect(Collectors.toList());
        return PageUtil.build(iPage, userInfos);
    }

    @AdminRequired
    @PutMapping("/user/{id}/password")
    @PermissionMeta(value = "修改用户密码", mount = false)
    public UpdatedVO changeUserPassword(@PathVariable @Positive(message = "{id.positive}") Integer id, @RequestBody @Validated ResetPasswordDTO validator) {
        adminService.changeUserPassword(id, validator);
        return new UpdatedVO(4);
    }

    @AdminRequired
    @DeleteMapping("/user/{id}")
    @PermissionMeta(value = "删除用户", mount = false)
    public DeletedVO deleteUser(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        adminService.deleteUser(id);
        return new DeletedVO(5);
    }

    @AdminRequired
    @PutMapping("/user/{id}")
    @PermissionMeta(value = "管理员更新用户信息", mount = false)
    public UpdatedVO updateUser(@PathVariable @Positive(message = "{id.positive}") Integer id, @RequestBody @Validated UpdateUserInfoDTO validator) {
        adminService.updateUserInfo(id, validator);
        return new UpdatedVO(6);
    }

    @AdminRequired
    @GetMapping("/group")
    @PermissionMeta(value = "查询所有权限组及其权限", mount = false)
    public PageResponseVO<GroupDO> getGroups(
            @RequestParam(name = "count", required = false, defaultValue = "10")
            @Min(value = 1, message = "{page.count.min}")
            @Max(value = 30, message = "{page.count.max}") Integer count,
            @RequestParam(name = "page", required = false, defaultValue = "0")
            @Min(value = 0, message = "{page.number.min}") Integer page) {
        IPage<GroupDO> iPage = adminService.getGroupPage(page, count);
        return PageUtil.build(iPage);
    }

    @AdminRequired
    @GetMapping("/group/all")
    @PermissionMeta(value = "查询所有权限组", mount = false)
    public List<GroupDO> getAllGroup() {
        return adminService.getAllGroups();
    }

    @AdminRequired
    @GetMapping("/group/{id}")
    @PermissionMeta(value = "查询一个权限组及其权限", mount = false)
    public GroupPermissionBO getGroup(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        return adminService.getGroup(id);
    }

    @AdminRequired
    @PostMapping("/group")
    @PermissionMeta(value = "新建权限组", mount = false)
    public CreatedVO createGroup(@RequestBody @Validated NewGroupDTO validator) {
        adminService.createGroup(validator);
        return new CreatedVO(15);
    }

    @AdminRequired
    @PutMapping("/group/{id}")
    @PermissionMeta(value = "更新一个权限组", mount = false)
    public UpdatedVO updateGroup(@PathVariable @Positive(message = "{id.positive}") Integer id,
                                 @RequestBody @Validated UpdateGroupDTO validator) {
        adminService.updateGroup(id, validator);
        return new UpdatedVO(7);
    }

    @AdminRequired
    @DeleteMapping("/group/{id}")
    @PermissionMeta(value = "删除一个权限组", mount = false)
    public DeletedVO deleteGroup(@PathVariable @Positive(message = "{id.positive}") Integer id) {
        adminService.deleteGroup(id);
        return new DeletedVO(8);
    }

    @AdminRequired
    @PostMapping("/permission/dispatch")
    @PermissionMeta(value = "分配单个权限", mount = false)
    public CreatedVO dispatchPermission(@RequestBody @Validated DispatchPermissionDTO validator) {
        adminService.dispatchPermission(validator);
        return new CreatedVO(9);
    }

    @AdminRequired
    @PostMapping("/permission/dispatch/batch")
    @PermissionMeta(value = "分配多个权限", mount = false)
    public CreatedVO dispatchPermissions(@RequestBody @Validated DispatchPermissionsDTO validator) {
        adminService.dispatchPermissions(validator);
        return new CreatedVO(9);
    }

    @AdminRequired
    @PostMapping("/permission/remove")
    @PermissionMeta(value = "删除多个权限", mount = false)
    public DeletedVO removePermissions(@RequestBody @Validated RemovePermissionsDTO validator) {
        adminService.removePermissions(validator);
        return new DeletedVO(10);
    }

}
