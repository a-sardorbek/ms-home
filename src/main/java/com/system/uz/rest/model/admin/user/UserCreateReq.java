package com.system.uz.rest.model.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateReq {
    @NotBlank(message = "username is mandatory")
    private String username;

    @NotBlank(message = "password is mandatory")
    @Size(min = 5, max = 20, message = "Password minimum or maximum length incorrect")
    private String password;

    @NotBlank(message = "fio is mandatory")
    private String fio;

    @NotBlank(message = "phone is mandatory")
    private String phone;
}
