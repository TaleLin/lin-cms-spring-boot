package com.lin.cms.merak.dto.user;

import com.lin.cms.autoconfigure.validator.Length;
import lombok.*;

import javax.validation.constraints.Email;

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
