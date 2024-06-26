package com.system.uz.rest.service;

import com.system.uz.enums.BotState;
import com.system.uz.enums.ClientState;
import com.system.uz.enums.TelegramMessageType;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Client;
import com.system.uz.rest.domain.admin.User;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.admin.client.ClientRes;
import com.system.uz.rest.model.admin.client.ClientUpdateReq;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.model.client.ClientCreateReq;
import com.system.uz.rest.model.telegram.TelegramSendMessage;
import com.system.uz.rest.repository.ClientRepository;
import com.system.uz.rest.repository.UserRepository;
import com.system.uz.rest.service.component.TelegramMessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final TelegramMessageService telegramMessageService;

    public void create(ClientCreateReq req) {
        Client client = new Client();
        client.setClientId(Utils.generateToken());
        client.setFio(req.getFio());
        client.setPhone(req.getPhone());
        client.setState(ClientState.NEW);

        if (Utils.isValidString(req.getDescription())) {
            client.setDescription(req.getDescription());
        }

        clientRepository.save(client);

        String message = String.format(
                TelegramMessageType.CLIENT_REQUEST_PHONE.getMessage(),
                "Заявка",
                client.getFio(),
                req.getPhone(),
                Objects.isNull(req.getDescription()) ? " " : req.getDescription());

        List<User> users = userRepository.findAll();
        if (!users.isEmpty()) {
            List<TelegramSendMessage> sendMessages = new ArrayList<>();
            for (User user : users) {
                if (Utils.isValidString(user.getTelegramChatId()) && user.getBotState().equals(BotState.ACTIVE)) {
                    TelegramSendMessage sendMessage = new TelegramSendMessage();
                    sendMessage.setChatId(user.getTelegramChatId());
                    sendMessage.setMessage(message);
                    sendMessages.add(sendMessage);
                }
            }
            telegramMessageService.sendListMessage(sendMessages);
        }

    }

    public void confirm(ClientUpdateReq req) {
        Optional<Client> optionalClient = clientRepository.findByClientId(req.getClientId());
        if (optionalClient.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Client client = optionalClient.get();
        client.setState(req.getIsConfirmed() ? ClientState.CONFIRMED : ClientState.NEW);

        clientRepository.save(client);
    }

    public ResponseEntity<ClientRes> getById(String clientId) {
        Optional<Client> optionalClient = clientRepository.findByClientId(clientId);
        if (optionalClient.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Client client = optionalClient.get();
        ClientRes clientRes = new ClientRes(
                client.getClientId(),
                client.getFio(),
                client.getPhone(),
                client.getDescription(),
                client.getState()
        );

        return ResponseEntity.ok(clientRes);
    }

    public ResponseEntity<PagingResponse<ClientRes>> getList(ClientState state, Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<Client> clients;
        if (Objects.nonNull(state)) {
            clients = clientRepository.findAllByState(state, pageable);
        } else {
            clients = clientRepository.findAll(pageable);
        }

        List<ClientRes> clientResList = new ArrayList<>();
        for (Client client : clients.getContent()) {
            clientResList.add(new ClientRes(
                    client.getClientId(),
                    client.getFio(),
                    client.getPhone(),
                    client.getDescription(),
                    client.getState()
            ));
        }

        PagingResponse<ClientRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(clientResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), clients.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }
}
