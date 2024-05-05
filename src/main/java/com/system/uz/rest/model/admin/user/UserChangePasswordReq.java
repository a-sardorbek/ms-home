package com.system.uz.rest.model.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordReq {
    @NotBlank(message = "code is mandatory")
    private String code;
    @NotBlank(message = "newPassword is mandatory")
    private String newPassword;
}
