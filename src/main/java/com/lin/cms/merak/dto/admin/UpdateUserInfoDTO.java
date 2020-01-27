package com.lin.cms.merak.dto.admin;

import com.lin.cms.autoconfigure.validator.LongList;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserInfoDTO {

    @LongList(min = 1, message = "{group.ids.long-list}")
    private List<Long> groupIds;
}
