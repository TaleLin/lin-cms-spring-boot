package io.github.talelin.latticy.dto.user;

import io.github.talelin.autoconfigure.validator.EqualField;
import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@NoArgsConstructor
@EqualField(srcField = "password", dstField = "confirmPassword", message = "{password.equal-field}")
public class RegisterDTO {

    @NotBlank(message = "{username.not-blank}")
    @Size(min = 2, max = 10, message = "{username.size}")
    private String username;

    @LongList(allowBlank = true, message = "{group.ids.long-list}")
    private List<Long> groupIds;

    @Email(message = "{email}")
    private String email;

    @NotBlank(message = "{new.password.not-blank}")
    @Pattern(regexp = "^[A-Za-z0-9_*&$#@]{6,22}$", message = "{new.password.pattern}")
    private String password;

    @NotBlank(message = "{confirm.password.not-blank}")
    private String confirmPassword;
}
