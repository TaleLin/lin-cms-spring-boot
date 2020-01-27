package com.lin.cms.merak.dto.admin;

import com.lin.cms.autoconfigure.validator.LongList;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class DispatchPermissionsDTO {

    @Positive(message = "{group.id.positive}")
    @NotNull(message = "{group.id.not-null}")
    private Long groupId;

    @LongList(message = "{permission.ids.long-list}")
    private List<Long> permissionIds;
}
