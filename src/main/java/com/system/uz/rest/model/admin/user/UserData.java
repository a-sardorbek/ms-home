package com.system.uz.rest.model.admin.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
    private String userId;
    private String username;
    private String password;
    private List<String> permissions;
}
