package com.system.uz.enums;

import com.system.uz.global.TelegramEmoji;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TelegramMessageType {
    CHANGE_PASSWORD_CONFIRM(
            TelegramEmoji.USER + "\t %s\n" +
            "\t %s\n\n" +
            "\t %s\n"
    ),

    CHANGE_PASSWORD_SUCCESS(
            TelegramEmoji.ADD + "\t %s\n"
    ),

    CHANGE_PASSWORD_ERROR(
            TelegramEmoji.ADD + "\t %s\n"
    ),

    FREQUENT_INFO(
            "\t%s\n" +
            "\t%s\n\n"
    ),

    USER_LANGUAGE_SAVED(
            TelegramEmoji.LANGUAGE + "\t%s\n" +
            TelegramEmoji.ADD + "\t%s\n"
    ),

    CLIENT_REQUEST_PHONE(
            TelegramEmoji.NEW + "\t %s\n\n" +
            TelegramEmoji.USER + "\t %s\n" +
            TelegramEmoji.PHONE + "\t %s\n" +
            TelegramEmoji.TEXT + "\t %s\n"
    );

    private final String message;
}
