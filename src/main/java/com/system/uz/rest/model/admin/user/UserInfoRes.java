package com.system.uz.rest.model.admin.user;

import com.system.uz.enums.BotState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoRes {
    private String userId;
    private String username;
    private String fio;
    private String phone;
    private BotState telegramState;
}
