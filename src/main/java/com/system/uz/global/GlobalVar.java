package com.system.uz.global;

import com.system.uz.enums.Lang;

public class GlobalVar {

    private final static ThreadLocal<Lang> LANG = ThreadLocal.withInitial(() -> Lang.RUS);
    private final static ThreadLocal<String> USER_ID = ThreadLocal.withInitial(() -> null);

    public static String getUserId() {
        return GlobalVar.USER_ID.get();
    }

    public static void setUserId(String userId) {
        GlobalVar.USER_ID.set(userId);
    }

    public static Lang getLANG() {
        return GlobalVar.LANG.get();
    }

    public static void setLANG(Lang lang) {
        GlobalVar.LANG.set(lang);
    }


}
