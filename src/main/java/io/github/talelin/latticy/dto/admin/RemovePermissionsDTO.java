package io.github.talelin.latticy.dto.admin;

import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * @author pedro@TaleLin
 */
@Data
public class RemovePermissionsDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Long groupId;

    @LongList(message = "{permission.ids.long-list}")
    private List<Long> permissionIds;
}
