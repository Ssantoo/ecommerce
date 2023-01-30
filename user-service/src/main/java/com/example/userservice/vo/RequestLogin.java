package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {

    @Email
    @NotNull(message = "이메일은 빈칸이 안됩니다.")
    @Size(min = 2, message = "이메일은 두글자 이상 입력해주세요")
    private String email;

    @NotNull(message = "비밀번호는 빈칸이 안됩니다.")
    @Size(min = 8, message = "비밀번호는 8글자 이상 입력해주세요")
    private String password;
}
