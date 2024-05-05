package com.system.uz.rest.model.admin.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateReq {
    @NotBlank(message = "clientId is mandatory")
    private String clientId;
    private Boolean isConfirmed = true;
}
