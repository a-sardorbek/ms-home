package com.system.uz.rest.controller.admin;

import com.system.uz.rest.model.admin.contact.ContactCreateReq;
import com.system.uz.rest.model.admin.contact.ContactRes;
import com.system.uz.rest.model.admin.contact.ContactUpdateReq;
import com.system.uz.rest.service.ContactService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + CONTACT)
@AllArgsConstructor
public class AdminContactController {

    private final ContactService contactService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ContactCreateReq req) {
        contactService.create(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping
    public ResponseEntity<?> update(@Valid @RequestBody ContactUpdateReq req) {
        contactService.update(req);
        return ResponseEntity.noContent().build();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<ContactRes> getById(@RequestParam("contactId") String contactId) {
        return contactService.getById(contactId);
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<List<ContactRes>> getList() {
        return contactService.getList();
    }


    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam("contactId") String contactId) {
        contactService.delete(contactId);
        return ResponseEntity.noContent().build();
    }


}
