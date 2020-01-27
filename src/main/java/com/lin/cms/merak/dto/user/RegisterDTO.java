package com.lin.cms.merak.dto.user;

import com.lin.cms.autoconfigure.validator.EqualField;
import com.lin.cms.autoconfigure.validator.LongList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

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


}
