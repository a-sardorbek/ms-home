package com.system.uz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TelegramLang {
    RUS("Русский \uD83C\uDDF7\uD83C\uDDFA"),
    UZB("O'zbek \uD83C\uDDFA\uD83C\uDDFF"),
    ENG("English \uD83C\uDDFA\uD83C\uDDF8");

    private final String name;

    public static TelegramLang getByName(final String name) {
        for (TelegramLang lang : TelegramLang.values()) {
            if (lang.getName().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return ENG;
    }

}
