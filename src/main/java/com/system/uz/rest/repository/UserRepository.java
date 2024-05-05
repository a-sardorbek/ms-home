package com.system.uz.rest.repository;

import com.system.uz.enums.BotState;
import com.system.uz.rest.domain.admin.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUserId(String userId);
    Optional<User> findByTelegramChatId(String chatId);
    Optional<User> findByPhone(String phone);
}
