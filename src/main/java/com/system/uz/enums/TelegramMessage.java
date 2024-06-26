package com.system.uz.enums;

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
                    return "⬇\uFE0F Iltimos tilni uzgartiring";
                case ENG:
                    return "⬇\uFE0F Change the language please";
                default:
                    return "⬇\uFE0F Измените язык, пожалуйста";
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

    FREQUENT_INFO {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Ma'lumot olish";
                case ENG:
                    return "Information";
                default:
                    return "Получить информацию";
            }
        }
    },

    PRODUCT_IMAGE_LIST {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Rasmlar to'plami";
                case ENG:
                    return "Pictures";
                default:
                    return "Коллекция фотографий";
            }
        }
    },

    OPEN_WEB_VIEW {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Veb Sahifamiz";
                case ENG:
                    return "Website";
                default:
                    return "Веб-сайт";
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
