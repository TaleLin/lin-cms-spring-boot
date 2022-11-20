package io.github.talelin.latticy.dto.admin;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 * 分配权限（多个）数据传输对象
 */
@Data
public class DispatchPermissionsDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Integer groupId;

    private List<Integer> permissionIds;
}
