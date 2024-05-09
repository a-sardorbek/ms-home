package com.system.uz.rest.controller.admin;

import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.user.*;
import com.system.uz.rest.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + USER)
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(ME)
    public ResponseEntity<UserInfoRes> getUserInfo() {
        return userService.getUserInfo();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<UserInfoRes>> getUserListInfo(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        return userService.getUserList(page, size);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> newUser(@Valid @RequestBody UserCreateReq req) {
        userService.newUser(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserUpdateReq req) {
        userService.updateUser(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping(TELEGRAM + ACTIVATION)
    public ResponseEntity<UserActivateTelegramRes> activateTelegram() {
        userService.activateTelegram();
        return ResponseEntity.noContent().build();
    }


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
//    })
//    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
//    @PostMapping(CHANGE_PASSWORD + SEND_OTP)
//    public ResponseEntity<?> changePasswordSendTelegram(@RequestParam(value = "userId", required = false) String userId) {
//        userService.changePasswordSendOtp(userId);
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
//    })
//    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
//    @PutMapping(CHANGE_PASSWORD)
//    public ResponseEntity<?> changePassword(@Valid @RequestBody UserChangePasswordReq req) {
//        userService.changePassword(req);
//        return ResponseEntity.noContent().build();
//    }


//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
//    })
//    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
//    @GetMapping(BY_ID)
//    public ResponseEntity<UserInfoRes> getUserInfoById(@RequestParam("userId") String userId) {
//        return userService.getUserInfoById(userId);
//    }


}
