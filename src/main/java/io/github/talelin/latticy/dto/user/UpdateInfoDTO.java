package io.github.talelin.latticy.dto.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

/**
 * @author pedro@TaleLin
 * 更新用户信息数据传输对象
 */
@NoArgsConstructor
@Data
public class UpdateInfoDTO {

    @Email(message = "{email}")
    private String email;

    @Length(min = 2, max = 10, message = "{nickname.length}")
    private String nickname;

    @Length(min = 2, max = 10, message = "{username.length}")
    private String username;

    @Length(max = 500, message = "{avatar.length}")
    private String avatar;
}
