package com.system.uz.rest.model.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateReq {

    @NotBlank(message = "fio is mandatory")
    private String fio;

    @NotBlank(message = "phone is mandatory")
    private String phone;

    private String description;

}
