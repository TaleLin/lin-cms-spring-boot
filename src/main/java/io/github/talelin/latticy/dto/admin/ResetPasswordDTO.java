package io.github.talelin.latticy.dto.admin;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author pedro@TaleLin
 */
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
@Data
public class ResetPasswordDTO {

    @NotBlank(message = "{password.new.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{password.new.pattern}")
    private String newPassword;

    @NotBlank(message = "{password.confirm.not-blank}")
    private String confirmPassword;
}
