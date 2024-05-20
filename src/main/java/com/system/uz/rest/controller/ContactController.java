package com.system.uz.rest.controller;

import com.system.uz.enums.Lang;
import com.system.uz.rest.model.contact.ContactWhiteRes;
import com.system.uz.rest.service.ContactService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + CONTACT)
@AllArgsConstructor
public class ContactController {

    private final ContactService contactService;


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language", dataTypeClass = Lang.class, paramType = "header", defaultValue = "UZB")
    })
    @GetMapping()
    public ResponseEntity<ContactWhiteRes> getContact() {
        return contactService.getWhiteContact();
    }


}
