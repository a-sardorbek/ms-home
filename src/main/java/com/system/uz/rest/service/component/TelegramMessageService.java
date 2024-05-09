package com.system.uz.rest.service.component;

import com.system.uz.config.TelegramBotConfig;
import com.system.uz.rest.model.telegram.TelegramSendMessage;
import com.system.uz.rest.service.TelegramService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
@AllArgsConstructor
public class TelegramMessageService {
    private final TelegramBotConfig telegramBotConfig;
    private final TelegramService telegramService;

    public void sendMessage(TelegramSendMessage sendMessage) {
        SendMessage message = new SendMessage(sendMessage.getChatId(), sendMessage.getMessage());
        try {
            telegramBotConfig.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendMessageCallBack(TelegramSendMessage sendMessage) {
        SendMessage message = new SendMessage(sendMessage.getChatId(), sendMessage.getMessage());
        message.setReplyMarkup(telegramService.getConfirmChangePasswordInlineButtons());
        try {
            telegramBotConfig.execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    public void sendListMessage(List<TelegramSendMessage> sendMessages) {
        for (TelegramSendMessage sendRequest : sendMessages) {
            SendMessage message = new SendMessage(sendRequest.getChatId(), sendRequest.getMessage());
            try {
                telegramBotConfig.execute(message);
            } catch (TelegramApiException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
