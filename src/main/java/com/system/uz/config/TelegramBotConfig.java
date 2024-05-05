package com.system.uz.config;

import com.system.uz.enums.TelegramLang;
import com.system.uz.enums.TelegramMessage;
import com.system.uz.env.TelegramBotEnv;
import com.system.uz.rest.domain.admin.User;
import com.system.uz.rest.domain.telegram.TelegramUser;
import com.system.uz.rest.repository.TelegramUserRepository;
import com.system.uz.rest.repository.UserRepository;
import com.system.uz.rest.service.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class TelegramBotConfig extends TelegramLongPollingBot {

    private final TelegramBotEnv botEnv;
    private final TelegramService telegramService;
    private final TelegramUserRepository telegramUserRepository;
    private final UserRepository userRepository;


    @Override
    public String getBotUsername() {
        return botEnv.getUsername();
    }

    @Override
    public String getBotToken() {
        return botEnv.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            SendMessage sendMessage = new SendMessage();

            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                String callbackData = callbackQuery.getData();
                execute(telegramService.getCallBackData(callbackData, String.valueOf(update.getCallbackQuery().getFrom().getId())));
            }

            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                String chatId = String.valueOf(update.getMessage().getChatId());

                TelegramLang lang = TelegramLang.UZB;

                Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
                if(optionalUser.isPresent()){
                    lang = Objects.nonNull(optionalUser.get().getLang()) ? optionalUser.get().getLang() : lang;
                }

                Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
                if(optionalTelegramUser.isPresent()){
                    lang = Objects.nonNull(optionalTelegramUser.get().getLang()) ? optionalTelegramUser.get().getLang() : lang;
                }

                if (messageText.equals("/start")) {
                    sendMessage.setChatId(chatId);
                    sendMessage.setText(TelegramMessage.START_MESSAGE.getName(lang));
                    sendMessage.setReplyMarkup(telegramService.getReplyButtons(lang, true));
                    execute(sendMessage);
                } else if (messageText.equals(TelegramMessage.CHANGE_LANGUAGE.getName(lang))) {
                    sendMessage.setText(TelegramMessage.CHANGE_LANGUAGE.getName(lang));
                    sendMessage.setReplyMarkup(telegramService.getLanguageInlineButtons());
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                }else {
                    execute(telegramService.defaultResponse(String.valueOf(update.getMessage().getChatId())));
                }

                //todo: frequesnt info

                //todo: product images also plan images

                //todo: request to qayta aloqa

            }

            if (update.hasMessage() && update.getMessage().hasContact()) {
                execute(telegramService.setContactInfo(String.valueOf(update.getMessage().getChatId()), update.getMessage().getContact()));
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
