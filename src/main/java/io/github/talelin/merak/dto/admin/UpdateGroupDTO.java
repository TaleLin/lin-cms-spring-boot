package io.github.talelin.merak.dto.admin;

import io.github.talelin.autoconfigure.validator.Length;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateGroupDTO {

    @NotBlank(message = "{group.name.not-blank}")
    @Length(min = 1, max = 60, message = "{group.name.length}")
    private String name;

    @Length(min = 1, max = 255, message = "{group.info.length}")
    private String info;
}
