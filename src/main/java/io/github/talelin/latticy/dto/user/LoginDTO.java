package io.github.talelin.latticy.dto.user;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "{user.username.not-blank}")
    private String username;

    @NotBlank(message = "{password.new-password.not-blank}")
    private String password;

}
