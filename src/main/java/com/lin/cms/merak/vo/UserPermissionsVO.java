package com.lin.cms.merak.vo;

import com.lin.cms.merak.model.UserDO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Setter
@Getter
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
