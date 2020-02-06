package io.github.talelin.merak.dto.admin;

import io.github.talelin.autoconfigure.validator.Length;
import lombok.Data;

@Data
public class UpdateGroupDTO {

    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{group.info.length}")
    private String info;
}
