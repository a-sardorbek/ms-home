package com.system.uz.rest.controller.auth;

import com.system.uz.rest.model.auth.SignInReq;
import com.system.uz.rest.model.auth.SignUpRes;
import com.system.uz.rest.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1)
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

//    @PostMapping(REGISTER)
//    public ResponseEntity<SignUpRes> signUp(@Valid @RequestBody SignUpReq data){
//        return userService.signUp(data);
//    }

    @PostMapping(LOGIN)
    public ResponseEntity<SignUpRes> signIn(@Valid @RequestBody SignInReq data){
        return userService.signIn(data);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PostMapping(LOGOUT)
    public ResponseEntity<?> signOut(){
        userService.signOut();
        return ResponseEntity.noContent().build();
    }

}
