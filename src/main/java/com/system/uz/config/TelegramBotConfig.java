package com.system.uz.config;

import com.system.uz.enums.BucketFolder;
import com.system.uz.enums.ImageType;
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
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
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
                String chatId = String.valueOf(update.getCallbackQuery().getFrom().getId());
                List<SendMessage> sendMessages = telegramService.getCallBackData(callbackData, chatId);
                if (sendMessages.size() == 1 && !sendMessages.get(0).getText().startsWith(BucketFolder.PRODUCT.name())) {
                    execute(sendMessages.get(0));
                } else {
                    for (SendMessage sendMess : sendMessages) {
                        SendPhoto sendPhoto = telegramService.getFileFromMinio(chatId, sendMess.getText());
                        if (Objects.nonNull(sendPhoto)) {
                            execute(sendPhoto);
                        }
                    }
                }
            }

            if (update.hasMessage() && update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                String chatId = String.valueOf(update.getMessage().getChatId());

                TelegramLang lang = TelegramLang.UZB;

                Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
                if (optionalUser.isPresent()) {
                    lang = Objects.nonNull(optionalUser.get().getLang()) ? optionalUser.get().getLang() : lang;
                } else {
                    Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
                    if (optionalTelegramUser.isPresent()) {
                        lang = Objects.nonNull(optionalTelegramUser.get().getLang()) ? optionalTelegramUser.get().getLang() : lang;
                    }
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
                } else if (messageText.equals(TelegramMessage.FREQUENT_INFO.getName(lang))) {
                    sendMessage.setText(TelegramMessage.FREQUENT_INFO.getName(lang));
                    sendMessage.setReplyMarkup(telegramService.getFrequentInlineButtons(lang));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                } else if (messageText.equals(TelegramMessage.PRODUCT_IMAGE_LIST.getName(lang))) {
                    sendMessage.setText(TelegramMessage.PRODUCT_IMAGE_LIST.getName(lang));
                    sendMessage.setReplyMarkup(telegramService.getProductInlineButtons(lang));
                    sendMessage.setChatId(chatId);
                    execute(sendMessage);
                } else {
                    execute(telegramService.defaultResponse(String.valueOf(update.getMessage().getChatId())));
                }

            }

            if (update.hasMessage() && update.getMessage().hasContact()) {
                System.out.println(update.getMessage());
                execute(telegramService.setContactInfo(String.valueOf(update.getMessage().getFrom().getId()), update.getMessage().getContact()));
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
