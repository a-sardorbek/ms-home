package com.system.uz.global;


import com.system.uz.enums.Lang;
import com.system.uz.enums.TelegramLang;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.rest.model.admin.criteria.PageSize;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class Utils {

    private static final String NUMERIC = "0123456789";
    private static final Random RANDOM = new SecureRandom();


    public static boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }

    public static boolean isNumericString(String str) {
        return str.matches("[0-9]+");
    }


    public static String checkPhone(String given) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < given.length(); i++) {
            if (!Character.isSpaceChar(given.charAt(i))) {
                sb.append(given.charAt(i));
            }
        }
        if (!sb.toString().startsWith("+998") || sb.toString().length() != 13) {
            throw new BadRequestException(MessageKey.PHONE_INCORRECT_FORMAT);
        }
        return sb.toString();
    }

    public static String getLanguage(String uz, String ru, String eng) {
        switch (GlobalVar.getLANG()) {
            case RUS:
                return ru;
            case ENG:
                return eng;
            default:
                return uz;
        }
    }

    public static String getLanguage(String uz, String ru, String eng, TelegramLang lang) {
        switch (lang) {
            case RUS:
                return ru;
            case ENG:
                return eng;
            default:
                return uz;
        }
    }

    public static String convertToString(List<String> stringList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String str : stringList) {
            stringBuilder.append(str);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public static boolean isEmpty(String str) {
        return !StringUtils.hasText(str);
    }

    public static DateTimeFormatter dateTimeFormatter() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static String generateRandomInteger(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(NUMERIC.charAt(RANDOM.nextInt(NUMERIC.length())));
        }
        return returnValue.toString();
    }

    public static PageSize validatePageSize(Integer page, Integer size) {
        if (Objects.isNull(page) || page == -1) {
            page = 0;
        } else {
            page = page - 1;
        }

        if (Objects.isNull(size) || size == -1) {
            size = 10;
        }

        return new PageSize(page, size);

    }

}
