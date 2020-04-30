package io.github.talelin.latticy.dto.admin;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author pedro@TaleLin
 */
@Data
public class DispatchPermissionDTO {

    @Min(value = 1, message = "{admin.dispatch-permission.group-id.min}")
    @NotNull(message = "{admin.dispatch-permission.group-id.not-null}")
    private Long groupId;

    @Min(value = 1, message = "{admin.dispatch-permission.permission-id.min}")
    @NotNull(message = "{admin.dispatch-permission.permission-id.not-null}")
    private Long permissionId;
}
