package com.system.uz.rest.model.admin.client;

import com.system.uz.enums.ClientState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRes {
    private String clientId;

    private String fio;

    private String phone;

    private String description;

    private ClientState state;
}
