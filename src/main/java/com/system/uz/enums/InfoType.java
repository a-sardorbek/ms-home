package com.system.uz.enums;

public enum InfoType {
    QUESTION {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Odatiy savollar";
                case ENG:
                    return "Frequently asked questions";
                default:
                    return "Часто задаваемые вопросы";
            }
        }
    },

    MATERIAL {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Hom Ashyo malumotlari";
                case ENG:
                    return "Raw materials information";
                default:
                    return "Информация о сырье";
            }
        }
    },

    ADVANTAGE {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Afzalliklari";
                case ENG:
                    return "Advantages";
                default:
                    return "Премиущества";
            }
        }
    },

    COMPANY {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Kompaniya Xaqida";
                case ENG:
                    return "About Company";
                default:
                    return "О компании";
            }
        }
    },

    MAINPAGE {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Asosiy";
                case ENG:
                    return "Main";
                default:
                    return "Оснавной";
            }
        }
    },


    CALLBACK {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Qayta aloqa";
                case ENG:
                    return "Order call back";
                default:
                    return "обратный звонок";
            }
        }
    },


    ABOUTUS {
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Biz haqimizda";
                case ENG:
                    return "About Us";
                default:
                    return "О нас";
            }
        }
    },

    PRODUCTION{
        @Override
        public String getName(TelegramLang language) {
            switch (language) {
                case UZB:
                    return "Ishlab chiqarish ma'lumotlari";
                case ENG:
                    return "Production information";
                default:
                    return "Информация о производстве";
            }
        }
    };

    public abstract String getName(TelegramLang language);

}
