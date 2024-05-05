package com.system.uz.enums;

import com.system.uz.global.TelegramEmoji;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TelegramMessageType {
    CHANGE_PASSWORD_OTP(
            TelegramEmoji.USER + "\t %s\n" +
            TelegramEmoji.TEXT + "\t %s\n\n" +
            TelegramEmoji.NEXT + "\t %s\n"
    ),

    ADMIN_USER_LANGUAGE_SAVED(
            TelegramEmoji.USER + "\t%s\n" +
            TelegramEmoji.LANGUAGE + "\t%s\n" +
            TelegramEmoji.ADD + "\t%s\n"
    ),

    USER_LANGUAGE_SAVED(
            TelegramEmoji.LANGUAGE + "\t%s\n" +
            TelegramEmoji.ADD + "\t%s\n"
    );

    private final String message;
}
