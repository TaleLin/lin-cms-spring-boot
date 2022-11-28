package io.github.talelin.latticy.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 分配权限（单个）数据传输对象
 */
@Data
public class DispatchPermissionDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Integer groupId;

    @Positive(message = "{permission.id.positive}")
    @NotNull(message = "{permission.id.not-null}")
    private Integer permissionId;
}
