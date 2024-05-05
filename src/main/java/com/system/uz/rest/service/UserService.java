package com.system.uz.rest.service;

import com.system.uz.enums.BotState;
import com.system.uz.enums.Permission;
import com.system.uz.enums.TelegramMessageType;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.exceptions.UnauthorizedException;
import com.system.uz.global.GlobalVar;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.admin.User;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.model.admin.user.*;
import com.system.uz.rest.model.auth.SignInReq;
import com.system.uz.rest.model.auth.SignUpReq;
import com.system.uz.rest.model.auth.SignUpRes;
import com.system.uz.rest.model.telegram.TelegramSendMessage;
import com.system.uz.rest.repository.UserRepository;
import com.system.uz.rest.service.component.JwtTokenProvider;
import com.system.uz.rest.service.component.TelegramMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TelegramMessageService telegramMessageService;

    public ResponseEntity<SignUpRes> signUp(SignUpReq data) {
        Optional<User> optionalUser = userRepository.findByUsername(data.getUsername());
        if (optionalUser.isPresent()) {
            throw new BadRequestException(MessageKey.USERNAME_ALREADY_EXISTS);
        }

        User user = new User();
        user.setUserId(Utils.generateToken());
        user.setUsername(data.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFio(data.getFio());
        user.setPhone(data.getPhone());
        user.setIsLoggedIn(true);
        User newUser = userRepository.save(user);

        String token = jwtTokenProvider.generateJwtToken(newUser.getUserId());

        return ResponseEntity.ok(new SignUpRes(token));
    }

    public ResponseEntity<SignUpRes> signIn(SignInReq data) {
        Optional<User> optionalUser = userRepository.findByUsername(data.getUsername());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(data.getPassword(), user.getPassword())) {
            throw new BadRequestException(MessageKey.INCORRECT_CREDENTIALS);
        }
        user.setIsLoggedIn(true);
        userRepository.save(user);

        String token = jwtTokenProvider.generateJwtToken(user.getUserId());

        return ResponseEntity.ok(new SignUpRes(token));
    }

    public void signOut() {
        Optional<User> optionalUser = userRepository.findByUserId(GlobalVar.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        User user = optionalUser.get();
        user.setIsLoggedIn(false);
        userRepository.save(user);
    }

    public UserData getAuthUser(String subject) {
        Optional<User> optionalUser = userRepository.findByUserId(subject);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        User user = optionalUser.get();

        if (!user.getIsLoggedIn()) {
            throw new UnauthorizedException(MessageKey.UNAUTHORIZED);
        }

        return new UserData(
                user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                List.of(
                        Permission.SYS_ADMIN_MODERN_HOUSE.name()
                ));
    }

    public ResponseEntity<UserInfoRes> getUserInfo() {
        Optional<User> optionalUser = userRepository.findByUserId(GlobalVar.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        User user = optionalUser.get();

        UserInfoRes userInfo = new UserInfoRes();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setFio(user.getFio());
        userInfo.setPhone(user.getPhone());

        return ResponseEntity.ok(userInfo);
    }

    public ResponseEntity<UserInfoRes> getUserInfoById(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        User user = optionalUser.get();

        UserInfoRes userInfo = new UserInfoRes();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        userInfo.setFio(user.getFio());
        userInfo.setPhone(user.getPhone());
        userInfo.setTelegramState(user.getBotState());

        return ResponseEntity.ok(userInfo);
    }

    public ResponseEntity<PagingResponse<UserInfoRes>> getUserList(Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<User> users = userRepository.findAll(pageable);
        List<UserInfoRes> userInfoResList = new ArrayList<>();
        for (User user : users.getContent()) {
            userInfoResList.add(new UserInfoRes(
                    user.getUserId(),
                    user.getUsername(),
                    user.getFio(),
                    user.getPhone(),
                    user.getBotState()
            ));
        }

        PagingResponse<UserInfoRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(userInfoResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), users.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }

    public void newUser(UserCreateReq req) {
        Optional<User> optionalUser = userRepository.findByUsername(req.getUsername());
        if (optionalUser.isPresent()) {
            throw new BadRequestException(MessageKey.USERNAME_ALREADY_EXISTS);
        }

        String phone = Utils.checkPhone(req.getPhone());

        Optional<User> optionalUserByPhone = userRepository.findByPhone(phone);
        if (optionalUserByPhone.isPresent()) {
            throw new BadRequestException(MessageKey.PHONE_ALREADY_EXISTS);
        }

        User user = new User();

        if (req.getPhone().equals(user.getPhone())) {
            throw new BadRequestException(MessageKey.PHONE_ALREADY_EXISTS);
        }

        user.setUserId(Utils.generateToken());
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFio(req.getFio());
        user.setPhone(req.getPhone());
        user.setIsLoggedIn(false);
        userRepository.save(user);
    }

    public void activateTelegram(UserIdRequest req) {
        Optional<User> optionalUser = userRepository.findByUserId(req.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        User user = optionalUser.get();
        user.setBotState(BotState.ACTIVE);
        userRepository.save(user);

    }

    public void updateUser(UserUpdateReq req) {
        Optional<User> optionalUser = userRepository.findByUserId(req.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (Utils.isValidString(req.getPhone())) {
            String phone = Utils.checkPhone(req.getPhone());


            Optional<User> optionalUserByPhone = userRepository.findByPhone(phone);
            if (optionalUserByPhone.isPresent()) {
                if (!Objects.equals(user.getId(), optionalUserByPhone.get().getId())) {
                    throw new BadRequestException(MessageKey.PHONE_ALREADY_EXISTS);
                }
            }
            user.setPhone(phone);
        }


        if (Utils.isValidString(req.getUsername())) {
            this.checkForUserNameExist(req.getUsername(), user.getUserId());
            user.setUsername(req.getUsername());
        }

        if (Utils.isValidString(req.getFio())) {
            user.setFio(req.getFio());
        }

    }


    public void changePasswordSendOtp(String userId) {
        Optional<User> optionalUser = userRepository.findByUserId(Utils.isValidString(userId) ? userId : GlobalVar.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        User user = optionalUser.get();

        if (user.getBotState().equals(BotState.ACTIVE) && Utils.isValidString(user.getTelegramChatId())) {
            String code = Utils.generateRandomInteger(5);
            user.setTelegramConfirmCode(passwordEncoder.encode(code));
            userRepository.save(user);

            String message = String.format(
                    TelegramMessageType.CHANGE_PASSWORD_OTP.getMessage(),
                    user.getFio(),
                    "Код для замена пароля",
                    code);

            TelegramSendMessage sendMessage = new TelegramSendMessage();
            sendMessage.setChatId(user.getTelegramChatId());
            sendMessage.setMessage(message);
            telegramMessageService.sendMessage(sendMessage);

        } else {
            throw new BadRequestException(MessageKey.USER_TELEGRAM_BOT_INACTIVE);
        }
    }

    public void changePassword(UserChangePasswordReq req) {
        Optional<User> optionalUser = userRepository.findByUserId(GlobalVar.getUserId());
        if (optionalUser.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        User user = optionalUser.get();

        if (passwordEncoder.matches(req.getCode(), user.getTelegramConfirmCode())) {
            user.setTelegramConfirmCode(Utils.generateRandomInteger(5));
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new BadRequestException(MessageKey.INCORRECT_CODE);
        }
    }

    private void checkForUserNameExist(String username, String userId) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getUserId().equals(userId)) {
                throw new BadRequestException(MessageKey.USERNAME_ALREADY_EXISTS);
            }
        }
    }

}
