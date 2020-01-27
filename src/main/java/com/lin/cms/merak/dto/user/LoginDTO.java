package com.lin.cms.merak.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
public class LoginDTO {

    @NotBlank(message = "{user.username.not-blank}")
    private String username;

    @NotBlank(message = "{password.new-password.not-blank}")
    private String password;
}
