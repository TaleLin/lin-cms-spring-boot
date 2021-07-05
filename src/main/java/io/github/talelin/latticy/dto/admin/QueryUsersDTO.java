package io.github.talelin.latticy.dto.admin;

import io.github.talelin.latticy.dto.query.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;

/**
 * @author Gadfly
 * @since 2021-06-28 18:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryUsersDTO extends BasePageDTO {

    @Min(value = 1, message = "{group.id.positive}")
    private Integer groupId;
}
