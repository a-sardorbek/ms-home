package com.system.uz.rest.controller;

import com.system.uz.rest.model.client.ClientCreateReq;
import com.system.uz.rest.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.system.uz.base.BaseUri.*;

@RestController
@RequestMapping(API_V1 + CLIENT)
@AllArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ClientCreateReq req) {
        clientService.create(req);
        return ResponseEntity.noContent().build();
    }

}
