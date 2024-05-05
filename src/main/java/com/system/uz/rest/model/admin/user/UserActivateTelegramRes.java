package com.system.uz.rest.model.admin.user;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserActivateTelegramRes {
    private String confirmCode;

    public UserActivateTelegramRes(String confirmCode) {
        this.confirmCode = confirmCode;
    }

}
