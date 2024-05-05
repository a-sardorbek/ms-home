package com.system.uz.rest.service;

import com.system.uz.enums.*;
import com.system.uz.env.MinioServiceEnv;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.admin.User;
import com.system.uz.rest.domain.telegram.TelegramUser;
import com.system.uz.rest.repository.TelegramUserRepository;
import com.system.uz.rest.repository.UserRepository;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TelegramService {
    private final MinioServiceEnv minioServiceEnv;
    private final MinioClient minioClient;
    private final UserRepository userRepository;
    private final TelegramUserRepository telegramUserRepository;

    public SendMessage defaultResponse(String chatId) {
        SendMessage sendMessage = new SendMessage();
        TelegramLang lang = TelegramLang.UZB;

        Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
        if (optionalUser.isPresent()) {
            lang = optionalUser.get().getLang() != null ? optionalUser.get().getLang() : TelegramLang.UZB;
            sendMessage.setText(TelegramMessage.DEFAULT_MESSAGE.getName(TelegramLang.UZB));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(getReplyButtons(lang, !Utils.isValidString(optionalUser.get().getPhone())));
            return sendMessage;
        }

        Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
        if (optionalTelegramUser.isPresent()) {
            lang = optionalTelegramUser.get().getLang() != null ? optionalTelegramUser.get().getLang() : TelegramLang.UZB;
            sendMessage.setText(TelegramMessage.DEFAULT_MESSAGE.getName(TelegramLang.UZB));
            sendMessage.setChatId(chatId);
            sendMessage.setReplyMarkup(getReplyButtons(lang, !Utils.isValidString(optionalTelegramUser.get().getPhone())));
            return sendMessage;
        }

        sendMessage.setText(TelegramMessage.DEFAULT_MESSAGE.getName(TelegramLang.UZB));
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setReplyMarkup(getReplyButtons(lang, true));
        return sendMessage;
    }

    public SendMessage setContactInfo(String chatId, Contact contact) {
        SendMessage sendMessage = new SendMessage();
        Optional<User> optionalUser = userRepository.findByPhone(contact.getPhoneNumber());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBotState(BotState.ACTIVE);
            user.setTelegramChatId(chatId);
            userRepository.save(user);
        } else {
            Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
            TelegramUser telegramUser;
            telegramUser = optionalTelegramUser.orElseGet(TelegramUser::new);
            telegramUser.setPhone(contact.getPhoneNumber());
            telegramUser.setFirstName(contact.getFirstName());
            telegramUser.setChatId(chatId);
            telegramUserRepository.save(telegramUser);
        }

        sendMessage.setText(TelegramMessage.CONTACT_SAVE_SUCCESS.getName(TelegramLang.UZB));
        sendMessage.setReplyMarkup(getReplyButtons(TelegramLang.UZB, false));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }


    public SendMessage getCallBackData(String data, String chatId) {
        TelegramLang lang;
        switch (data) {
            case "UZB":
                lang = TelegramLang.UZB;
                break;
            case "ENG":
                lang = TelegramLang.ENG;
                break;
            default:
                lang = TelegramLang.RUS;
        }

        Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLang(lang);
            user.setBotState(BotState.ACTIVE);
            user.setTelegramChatId(chatId);
            userRepository.save(user);
        } else {
            Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
            TelegramUser telegramUser;
            telegramUser = optionalTelegramUser.orElseGet(TelegramUser::new);
            telegramUser.setChatId(chatId);
            telegramUser.setLang(lang);
            telegramUserRepository.save(telegramUser);
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(String.format(TelegramMessageType.USER_LANGUAGE_SAVED.getMessage(), lang.getName(), "#success"));
        sendMessage.setReplyMarkup(getReplyButtons(lang, false));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }


    public ReplyKeyboardMarkup getReplyButtons(TelegramLang lang, boolean showContact) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        if (showContact) {
            KeyboardButton contactButton = new KeyboardButton();
            contactButton.setText(TelegramMessage.SHARE_CONTACT.getName(lang));
            contactButton.setRequestContact(true);
            row.add(contactButton);
        } else {
            KeyboardButton languageButtons = new KeyboardButton();
            languageButtons.setText(TelegramMessage.CHANGE_LANGUAGE.getName(lang));
            row.add(languageButtons);
        }

        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        return keyboardMarkup;
    }

    public InlineKeyboardMarkup getLanguageInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton uzbek = new InlineKeyboardButton(TelegramLang.UZB.getName());
        uzbek.setCallbackData("UZB");

        InlineKeyboardButton russian = new InlineKeyboardButton(TelegramLang.RUS.getName());
        russian.setCallbackData("RUS");

        InlineKeyboardButton english = new InlineKeyboardButton(TelegramLang.ENG.getName());
        english.setCallbackData("ENG");

        inlineKeyboardMarkup.setKeyboard(List.of(List.of(uzbek, russian, english)));

        return inlineKeyboardMarkup;
    }


    public void sendPhotoFromMinio(String chatId, String objectName, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);

        try {
            // Get the image from Minio
            InputStream obj = minioClient.getObject(GetObjectArgs.builder().bucket(minioServiceEnv.getBucket()).object(objectName).build());
            InputFile inputFile = new InputFile(obj, objectName);

            // Set the image and caption
            sendPhoto.setPhoto(inputFile);
            sendPhoto.setCaption(caption);

//            execute(sendPhoto);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
