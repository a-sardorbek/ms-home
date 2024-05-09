package com.system.uz.rest.service;

import com.system.uz.enums.*;
import com.system.uz.env.MinioServiceEnv;
import com.system.uz.global.TelegramEmoji;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Category;
import com.system.uz.rest.domain.FrequentInfo;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.domain.Product;
import com.system.uz.rest.domain.admin.User;
import com.system.uz.rest.domain.telegram.TelegramUser;
import com.system.uz.rest.repository.*;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
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
    private final FrequentInfoRepository frequentInfoRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

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
        String phone = "+"+contact.getPhoneNumber();
        Optional<User> optionalUser = userRepository.findByPhone(phone);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setBotState(BotState.ACTIVE);
            user.setTelegramChatId(chatId);
            userRepository.save(user);
        } else {
            Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
            TelegramUser telegramUser;
            telegramUser = optionalTelegramUser.orElseGet(TelegramUser::new);
            telegramUser.setPhone(phone);
            telegramUser.setFirstName(contact.getFirstName());
            telegramUser.setChatId(chatId);
            telegramUserRepository.save(telegramUser);
        }

        sendMessage.setText(TelegramMessage.CONTACT_SAVE_SUCCESS.getName(TelegramLang.UZB));
        sendMessage.setReplyMarkup(getReplyButtons(TelegramLang.UZB, false));
        sendMessage.setChatId(chatId);
        return sendMessage;
    }


    public List<SendMessage> getCallBackData(String data, String chatId) {
        TelegramLang lang;
        switch (data) {
            case "ENG":
                lang = TelegramLang.ENG;
                break;
            case "RUS":
                lang = TelegramLang.RUS;
                break;
            case "UZB":
                lang = TelegramLang.UZB;
                break;
            case "CONFIRM_PASSWORD":
                return confirmPassword(chatId);
            case "QUESTION":
                return frequentInfo(chatId, InfoType.QUESTION);
            case "MATERIAL":
                return frequentInfo(chatId, InfoType.MATERIAL);
            case "PRODUCTION":
                return frequentInfo(chatId, InfoType.PRODUCTION);
            default:
                return productList(chatId, data);
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
        return List.of(sendMessage);
    }

    private List<SendMessage> confirmPassword(String chatId) {
        Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(user.getConfirmPassword());
            userRepository.save(user);
            sendMessage.setText(String.format(TelegramMessageType.CHANGE_PASSWORD_SUCCESS.getMessage(), "Успешно подтверждено"));
            sendMessage.setChatId(chatId);
        } else {
            sendMessage.setText(String.format(TelegramMessageType.CHANGE_PASSWORD_ERROR.getMessage(), "Пользователь не найден"));
            sendMessage.setChatId(chatId);
        }

        return List.of(sendMessage);
    }

    private List<SendMessage> productList(String chatId, String data) {
        List<Product> products = productRepository.findTop30ByCategoryIdOrderByIdDesc(data);
        List<String> productIds = new ArrayList<>();
        List<SendMessage> sendMessages = new ArrayList<>();

        for (Product product : products) {
            productIds.add(product.getProductId());
        }

        List<Image> images = imageRepository.findAllByProductIdIn(productIds);

        for (Image image : images) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(image.getFullPath());
            sendMessages.add(sendMessage);
        }

        return sendMessages;
    }

    private List<SendMessage> frequentInfo(String chatId, InfoType type) {
        SendMessage sendMessage = new SendMessage();

        TelegramLang lang = TelegramLang.UZB;
        Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            lang = user.getLang() != null ? user.getLang() : TelegramLang.UZB;

        }

        Optional<TelegramUser> optionalTelegramUser = telegramUserRepository.findByChatId(chatId);
        if (optionalTelegramUser.isPresent()) {
            TelegramUser telegramUser = optionalTelegramUser.get();
            lang = telegramUser.getLang() != null ? telegramUser.getLang() : TelegramLang.UZB;
        }


        List<FrequentInfo> frequentInfos = frequentInfoRepository.findTop10ByTypeOrderByIdDesc(type);
        List<String> texts = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < frequentInfos.size(); i++) {
            index = i + 1;
            String text = String.format(
                    TelegramMessageType.FREQUENT_INFO.getMessage(),
                    index + ") " + Utils.getLanguage(frequentInfos.get(i).getQuestionUz(), frequentInfos.get(i).getQuestionRu(), frequentInfos.get(i).getQuestionEng(), lang),
                    Utils.getLanguage(frequentInfos.get(i).getAnswerUz(), frequentInfos.get(i).getAnswerRu(), frequentInfos.get(i).getAnswerEng(), lang));
            texts.add(text);
        }

        sendMessage.setText(texts.isEmpty() ? TelegramMessage.DEFAULT_MESSAGE.getName(lang) : Utils.convertToString(texts));
        sendMessage.setChatId(chatId);
        return List.of(sendMessage);
    }


    public ReplyKeyboardMarkup getReplyButtons(TelegramLang lang, boolean showContact) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        if (showContact) {
            KeyboardRow row = new KeyboardRow();
            KeyboardButton contactButton = new KeyboardButton();
            contactButton.setText(TelegramMessage.SHARE_CONTACT.getName(lang));
            contactButton.setRequestContact(true);
            row.add(contactButton);
            keyboard.add(row);
        } else {
            KeyboardRow row1 = new KeyboardRow();
            KeyboardButton languageButtons = new KeyboardButton();
            languageButtons.setText(TelegramMessage.CHANGE_LANGUAGE.getName(lang));
            row1.add(languageButtons);


            KeyboardRow row2 = new KeyboardRow();
            KeyboardButton frequentButtons = new KeyboardButton();
            frequentButtons.setText(TelegramMessage.FREQUENT_INFO.getName(lang));
            row2.add(frequentButtons);

            KeyboardRow row3 = new KeyboardRow();
            KeyboardButton productButtons = new KeyboardButton();
            productButtons.setText(TelegramMessage.PRODUCT_IMAGE_LIST.getName(lang));
            row3.add(productButtons);

            keyboard.add(row3);
            keyboard.add(row2);
            keyboard.add(row1);
        }

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

    public InlineKeyboardMarkup getFrequentInlineButtons(TelegramLang lang) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> questions = new ArrayList<>();

        InlineKeyboardButton question = new InlineKeyboardButton(InfoType.QUESTION.getName(lang));
        question.setCallbackData("QUESTION");
        questions.add(question);

        List<InlineKeyboardButton> materials = new ArrayList<>();
        InlineKeyboardButton material = new InlineKeyboardButton(InfoType.MATERIAL.getName(lang));
        material.setCallbackData("MATERIAL");
        materials.add(material);

        List<InlineKeyboardButton> productions = new ArrayList<>();
        InlineKeyboardButton production = new InlineKeyboardButton(InfoType.PRODUCTION.getName(lang));
        production.setCallbackData("PRODUCTION");
        productions.add(production);

        inlineKeyboardMarkup.setKeyboard(List.of(questions, materials, productions));

        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getProductInlineButtons(TelegramLang lang) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> categoryButtons = new ArrayList<>();

        List<Category> categories = categoryRepository.findAllByStatus(Status.ACTIVE);
        for (Category category : categories) {
            InlineKeyboardButton button = new InlineKeyboardButton(Utils.getLanguage(category.getTitleUz(), category.getTitleRu(), category.getTitleEng(), lang));
            button.setCallbackData(category.getCategoryId());

            List<InlineKeyboardButton> row = new ArrayList<>();
            row.add(button);
            categoryButtons.add(row);
        }

        inlineKeyboardMarkup.setKeyboard(categoryButtons);

        return inlineKeyboardMarkup;
    }


    public InlineKeyboardMarkup getConfirmChangePasswordInlineButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton confirm = new InlineKeyboardButton();
        confirm.setText(" " + TelegramEmoji.ADD);
        confirm.setCallbackData("CONFIRM_PASSWORD");

        inlineKeyboardMarkup.setKeyboard(List.of(List.of(confirm)));

        return inlineKeyboardMarkup;
    }


    public SendPhoto getFileFromMinio(String chatId, String objectName) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);

        try {
            // Get the image from Minio
            InputStream obj = minioClient.getObject(GetObjectArgs.builder().bucket(minioServiceEnv.getBucket()).object(objectName).build());
            InputFile inputFile = new InputFile(obj, objectName);

            // Set the image and caption
            sendPhoto.setPhoto(inputFile);

            return sendPhoto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

}
