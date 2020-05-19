package io.github.talelin.latticy.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * @author pedro@TaleLin
 */
@NoArgsConstructor
@Data
public class UpdateInfoDTO {

    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{nickname.size}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{username.size}")
    private String username;

    @Length(max = 500, message = "{avatar.size}")
    private String avatar;
}
