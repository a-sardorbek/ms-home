package com.system.uz.enums;

import com.system.uz.global.TelegramEmoji;

import java.io.Serializable;

public enum TelegramMessage implements Serializable {

    SHARE_CONTACT {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Tel. Raqam yuborish";
                case ENG:
                    return "Share Contact";
                default:
                    return "Поделиться контактом";
            }
        }
    },

    CONTACT_SAVE_SUCCESS {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Muvaffaqiyatli roʻyxatdan oʻtkazildi";
                case ENG:
                    return "Successfully registerd";
                default:
                    return "Успешная регистрация";
            }
        }
    },

    DEFAULT_MESSAGE {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "⬇\uFE0F Pastda joylashgan tugmani bosing";
                case ENG:
                    return "⬇\uFE0F Click the buttons below";
                default:
                    return "⬇\uFE0F Нажмите кнопки ниже";
            }
        }
    },

    START_MESSAGE {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "\uD83D\uDE42 Assalomu aleykum\n⬇\uFE0F Telefon raqam orqali ro'yhattan o'ting";
                case ENG:
                    return "\uD83D\uDE42 Hello\n⬇\uFE0F Share your contact for registration ";
                default:
                    return "\uD83D\uDE42 Здравствуйте\n⬇\uFE0F Поделитесь контактом для регистрации";
            }
        }
    },

    REQUEST_PHONE_CALL {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return TelegramEmoji.PHONE + " Konsultatsiyaga yozdiriling";
                case ENG:
                    return TelegramEmoji.PHONE + " Konsultatsiyaga yozdiriling";
                default:
                    return TelegramEmoji.PHONE + " Запишитесь на консультацию";
            }
        }
    },

    CHANGE_LANGUAGE {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Tilni o'zgartirish";
                case ENG:
                    return "Change Language";
                default:
                    return "Изменить язык";
            }
        }
    };

    public abstract String getName(TelegramLang language);


}
