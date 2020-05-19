package io.github.talelin.latticy.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@NoArgsConstructor
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
public class ChangePasswordDTO {

    @NotBlank(message = "{new.password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{new.password.pattern}")
    private String newPassword;

    @NotBlank(message = "{confirm.password.not-blank}")
    private String confirmPassword;

    @NotBlank(message = "{old.password.not-blank}")
    private String oldPassword;

}
