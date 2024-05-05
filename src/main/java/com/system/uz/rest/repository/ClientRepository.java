package com.system.uz.rest.repository;

import com.system.uz.enums.ClientState;
import com.system.uz.rest.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);

    Page<Client> findAllByState(ClientState state, Pageable pageable);
}
