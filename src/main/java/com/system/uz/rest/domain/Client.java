package com.system.uz.rest.domain;

import com.system.uz.base.BaseEntity;
import com.system.uz.enums.ClientState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "clients")
public class Client extends BaseEntity {

    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;

    @Column(name = "fio")
    private String fio;

    @Column(name = "phone")
    private String phone;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ClientState state;
}
