package io.github.talelin.merak.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
@NoArgsConstructor
@EqualField(srcField = "newPassword", dstField = "confirmPassword", message = "{password.equal-field}")
public class ChangePasswordDTO {

    @NotBlank(message = "{password.new-password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{password.new-password.pattern}")
    private String newPassword;

    @NotBlank(message = "{password.confirm-password.not-blank}")
    private String confirmPassword;

    @NotBlank(message = "{password.old-password.not-blank}")
    private String oldPassword;
}
