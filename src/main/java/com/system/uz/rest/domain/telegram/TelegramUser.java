package com.system.uz.rest.domain.telegram;

import com.system.uz.base.BaseEntity;
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
@Table(name = "telegram_users")
public class TelegramUser extends BaseEntity {

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "lang")
    @Enumerated(EnumType.STRING)
    private TelegramLang lang = TelegramLang.UZB;

}
