package com.system.uz.rest.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {
    @NotBlank(message = "Username is mandatory")
    private String username;

    // todo: annotation for password validation
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "FIO is mandatory")
    private String fio;

    @NotBlank(message = "Phone is mandatory")
    private String phone;
}
