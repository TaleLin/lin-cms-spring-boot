package io.github.talelin.merak.vo;

import io.github.talelin.merak.model.UserDO;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * 用户 + 权限 view object
 */
@Data
public class UserPermissionVO {

    private Long id;

    private String nickname;

    private String avatar;

    private Boolean admin;

    private String email;

    private List permissions;

    public UserPermissionVO() {
    }

    public UserPermissionVO(UserDO userDO, List permissions) {
        BeanUtils.copyProperties(userDO, this);
        this.permissions = permissions;
    }

    public UserPermissionVO(UserDO userDO) {
        BeanUtils.copyProperties(userDO, this);
    }
}
