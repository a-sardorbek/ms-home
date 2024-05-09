package com.system.uz.rest.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordReq {
    @NotBlank(message = "Phone is mandatory")
    private String phone;

    @NotBlank(message = "New Password is mandatory")
    @Size(min = 5, max = 20, message = "Password minimum or maximum length incorrect")
    private String password;
}
