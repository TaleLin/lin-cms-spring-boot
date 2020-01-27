package com.lin.cms.merak.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lin.cms.merak.model.UserDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoVO {

    private Long id;

    /**
     * 用户名，唯一
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像url
     */
    private String avatar;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 分组
     */
    private List groups;

    public UserInfoVO(UserDO user, List groups) {
        BeanUtil.copyProperties(user, this);
        this.groups = groups;
    }
}
