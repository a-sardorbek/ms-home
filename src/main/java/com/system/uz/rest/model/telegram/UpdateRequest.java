package com.system.uz.rest.model.telegram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateRequest {

    private String chatId;
    private String message;
    private String firstName;
    private String phone;
    private Long fromUserId;
    private Long contactUserId;
    private Long myChatMemberUserId;
    private Boolean hasMessage;

}
