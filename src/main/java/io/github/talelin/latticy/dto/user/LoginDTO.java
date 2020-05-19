package io.github.talelin.latticy.dto.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author pedro@TaleLin
 * @author Juzi@TaleLin
 */
@Data
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "{username.not-blank}")
    private String username;

    @NotBlank(message = "{password.new.not-blank}")
    private String password;
}
