package com.system.uz.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Lang {

    RUS("RUS"),
    UZB("UZB"),
    ENG("ENG");

    private final String name;

    public static Lang getByName(final String name) {
        for (Lang lang : Lang.values()) {
            if (lang.getName().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return ENG;
    }
}
