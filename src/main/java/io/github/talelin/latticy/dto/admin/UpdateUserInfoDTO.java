package io.github.talelin.latticy.dto.admin;

import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;

import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
public class UpdateUserInfoDTO {

    @LongList(min = 1, message = "{group.ids.long-list}")
    private List<Integer> groupIds;
}
