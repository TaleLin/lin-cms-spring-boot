package io.github.talelin.latticy.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Setter
@Getter
@NoArgsConstructor
@EqualField(srcField = "password", dstField = "confirmPassword", message = "{password.equal-field}")
public class RegisterDTO {

    @NotBlank(message = "{user.register.username.not-blank}")
    @Size(min = 2, max = 10, message = "{user.register.username.size}")
    private String username;

    @LongList(allowBlank = true, message = "{user.register.group-ids.long-list}")
    private List<Long> groupIds;

    @Email(message = "{email}")
    private String email;

    @NotBlank(message = "{password.new-password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{password.new-password.pattern}")
    private String password;

    @NotBlank(message = "{password.confirm-password.not-blank}")
    private String confirmPassword;

    @Override
    public String toString() {
        return "RegisterDTO{" +
                "username='" + username + '\'' +
                ", groupIds=" + groupIds +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
