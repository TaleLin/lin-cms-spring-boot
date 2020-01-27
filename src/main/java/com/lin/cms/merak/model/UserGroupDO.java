package com.lin.cms.merak.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author pedro
 * @since 2019-11-30
 */
@TableName("lin_user_group")
@Data
public class UserGroupDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 分组id
     */
    private Long groupId;

    public UserGroupDO(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }
}
