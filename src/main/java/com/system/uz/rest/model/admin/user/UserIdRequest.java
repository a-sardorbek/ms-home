package com.system.uz.rest.model.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserIdRequest {
    @NotBlank(message = "userId is mandatory")
    private String userId;
}
