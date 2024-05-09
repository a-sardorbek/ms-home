package com.system.uz.rest.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordReq {
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @NotBlank(message = "New Password is mandatory")
    private String password;
}
