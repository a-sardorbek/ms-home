package com.system.uz.rest.controller.admin;

import com.system.uz.enums.ClientState;
import com.system.uz.global.PagingResponse;
import com.system.uz.rest.model.admin.client.ClientRes;
import com.system.uz.rest.model.admin.client.ClientUpdateReq;
import com.system.uz.rest.service.ClientService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + ADMIN + CLIENT)
@AllArgsConstructor
public class AdminClientController {

    private final ClientService clientService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @PutMapping(CONFIRM)
    public ResponseEntity<?> confirm(@Valid @RequestBody ClientUpdateReq req) {
        clientService.confirm(req);
        return ResponseEntity.noContent().build();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(BY_ID)
    public ResponseEntity<ClientRes> getById(@RequestParam("clientId") String clientId) {
        return clientService.getById(clientId);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Token", required = true, paramType = "header", dataTypeClass = String.class),
    })
    @PreAuthorize("hasAuthority(T(com.system.uz.enums.Permission).SYS_ADMIN_MODERN_HOUSE)")
    @GetMapping(LIST)
    public ResponseEntity<PagingResponse<ClientRes>> getList(
            @RequestParam(value = "state", required = false) ClientState state,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        return clientService.getList(state, page, size);
    }


}
