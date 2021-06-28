package io.github.talelin.latticy.dto.query;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Gadfly
 */

@Data
public class BasePageDTO {

    @Min(value = 1, message = "{page.count.min}")
    @Max(value = 30, message = "{page.count.max}")
    private Integer count = 10;

    @Min(value = 0, message = "{page.number.min}")
    private Integer page = 0;

}
