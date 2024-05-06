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

    FREQUENT_INFO(
            "\t%s\n" +
            "\t%s\n\n"
    ),

    USER_LANGUAGE_SAVED(
            TelegramEmoji.LANGUAGE + "\t%s\n" +
            TelegramEmoji.ADD + "\t%s\n"
    );

    private final String message;
}
