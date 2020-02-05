package com.lin.cms.merak.vo;

import com.lin.cms.merak.model.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 用户 + 权限 view object
 */
@Data
public class UserPermissionsVO {

    private Long id;

    private String nickname;

    private String avatar;

    private Boolean admin;

    private String email;

    private List permissions;

    public UserPermissionsVO() {
    }

    public UserPermissionsVO(UserDO userDO, List permissions) {
        BeanUtils.copyProperties(userDO, this);
        this.permissions = permissions;
    }

    public UserPermissionsVO(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
    }
}
