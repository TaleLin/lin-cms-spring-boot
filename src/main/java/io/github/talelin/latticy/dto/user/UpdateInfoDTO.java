package io.github.talelin.latticy.dto.user;

import io.github.talelin.autoconfigure.validator.Length;
import lombok.*;

import javax.validation.constraints.Email;

/**
 * @author pedro@TaleLin
 */
@NoArgsConstructor
@Data
public class UpdateInfoDTO {

    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{user.nickname.size}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{user.username.size}")
    private String username;

    @Length(max = 500, message = "{user.avatar.size}")
    private String avatar;
}
