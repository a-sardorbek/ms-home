package com.system.uz.rest.domain.admin;

import com.system.uz.base.BaseEntity;
import com.system.uz.enums.BotState;
import com.system.uz.enums.Lang;
import com.system.uz.enums.TelegramLang;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fio", nullable = false)
    private String fio;

    // ex: +998901234567
    @Column(name = "phone")
    private String phone;

    @Column(name = "is_logged_in")
    private Boolean isLoggedIn = false;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "telegram_confirm_code")
    private String telegramConfirmCode;

    @Column(name = "bot_state")
    @Enumerated(EnumType.STRING)
    private BotState botState = BotState.INACTIVE;

    @Column(name = "lang")
    @Enumerated(EnumType.STRING)
    private TelegramLang lang = TelegramLang.UZB;

}
