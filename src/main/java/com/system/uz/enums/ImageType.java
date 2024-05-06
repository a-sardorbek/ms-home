package com.system.uz.enums;

import java.io.Serializable;

public enum ImageType implements Serializable {
    PLAN {
        @Override
        public String getName(Lang language) {
            switch (language) {
                case RUS:
                    return "Планировка дома";
                case ENG:
                    return "House Scheme";
                case UZB:
                    return "Uy Chizmasi";
                default:
                    return "Планировка дома";
            }
        }
    },
    PROJECT {
        @Override
        public String getName(Lang language) {
            switch (language) {
                case UZB:
                case ENG:
                case RUS:
                    return "Project";
                default:
                    return "";
            }
        }
    },

    BLOG {
        @Override
        public String getName(Lang language) {
            switch (language) {
                case UZB:
                case ENG:
                case RUS:
                    return "BLOG";
                default:
                    return "";
            }
        }
    },

    FILE {
        @Override
        public String getName(Lang language) {
            switch (language) {
                case UZB:
                case ENG:
                case RUS:
                    return "FILE";
                default:
                    return "";
            }
        }
    };


    public abstract String getName(Lang language);



}
