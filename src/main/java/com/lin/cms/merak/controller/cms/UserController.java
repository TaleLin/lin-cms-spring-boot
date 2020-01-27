package com.lin.cms.merak.controller.cms;

import com.lin.cms.core.annotation.AdminRequired;
import com.lin.cms.core.annotation.LoginRequired;
import com.lin.cms.core.annotation.RefreshRequired;
import com.lin.cms.core.annotation.RouteMeta;
import com.lin.cms.merak.common.LocalUser;
import com.lin.cms.merak.common.utils.ResponseUtil;
import com.lin.cms.merak.model.GroupDO;
import com.lin.cms.merak.service.GroupService;
import com.lin.cms.merak.service.UserIdentityService;
import com.lin.cms.merak.vo.UnifyResponseVO;
import com.lin.cms.merak.model.UserDO;
import com.lin.cms.merak.vo.UserInfoVO;
import com.lin.cms.merak.vo.UserPermissionsVO;
import com.lin.cms.merak.service.UserService;
import com.lin.cms.core.token.DoubleJWT;
import com.lin.cms.merak.dto.user.*;
import com.lin.cms.core.token.Tokens;
import com.lin.cms.autoconfigure.exception.NotFoundException;
import com.lin.cms.autoconfigure.exception.ParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by lin on 2019/05/23.
 * License MIT
 */

@RestController
@RequestMapping("/cms/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private DoubleJWT jwt;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @AdminRequired
    public UnifyResponseVO<String> register(@RequestBody @Validated RegisterDTO validator) {
        userService.createUser(validator);
        return ResponseUtil.generateUnifyResponse(9);
    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated LoginDTO validator) {
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException("user not found", 10021);
        }
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        if (!valid)
            throw new ParameterException("username or password is fault", 10031);
        return jwt.generateTokens(user.getId());
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    @LoginRequired
    public UnifyResponseVO update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return ResponseUtil.generateUnifyResponse(4);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change_password")
    @LoginRequired
    public UnifyResponseVO updatePassword(@RequestBody @Validated ChangePasswordDTO validator) {
        userService.changeUserPassword(validator);
        return ResponseUtil.generateUnifyResponse(2);
    }

    /**
     * 刷新令牌
     */
    @GetMapping("/refresh")
    @RefreshRequired
    public Tokens refreshToken() {
        UserDO user = LocalUser.getLocalUser();
        return jwt.generateTokens(user.getId());
    }

    /**
     * 查询拥有权限
     */
    @GetMapping("/permissions")
    @LoginRequired
    @RouteMeta(permission = "查询自己拥有的权限", module = "用户", mount = true)
    public UserPermissionsVO getPermissions() {
        UserDO user = LocalUser.getLocalUser();
        boolean admin = groupService.checkIsRootByUserId(user.getId());
        List<Map<String, List<Map<String, String>>>> permissions = userService.getStructualUserPermissions(user.getId());
        UserPermissionsVO userPermissions = new UserPermissionsVO(user, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 查询自己信息
     */
    @LoginRequired
    @RouteMeta(permission = "查询自己信息", module = "用户", mount = true)
    @GetMapping("/information")
    public UserInfoVO getInformation() {
        UserDO user = LocalUser.getLocalUser();
        List<GroupDO> groups = groupService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }
}
