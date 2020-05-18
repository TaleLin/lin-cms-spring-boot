package io.github.talelin.latticy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@TableName("lin_group_permission")
public class GroupPermissionDO implements Serializable {

    private static final long serialVersionUID = -358487811336536495L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分组id
     */
    private Long groupId;

    /**
     * 权限id
     */
    private Long permissionId;

    public GroupPermissionDO(Long groupId, Long permissionId) {
        this.groupId = groupId;
        this.permissionId = permissionId;
    }
}
