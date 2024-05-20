package com.system.uz.rest.domain;

import com.system.uz.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "deleted_at IS NULL")
@Table(name = "contacts")
public class Contact extends BaseEntity {
    @Column(name = "contact_id", nullable = false, unique = true)
    private String contactId;

    @Column(name = "first_phone")
    private String firstPhone;

    @Column(name = "second_phone")
    private String secondPhone;

    @Column(name = "email")
    private String email;

    @Column(name = "main_office")
    private String mainOffice;

    @Column(name = "production_office")
    private String productionOffice;

    @Column(name = "description_uz", columnDefinition = "TEXT")
    private String descriptionUz;

    @Column(name = "description_ru", columnDefinition = "TEXT")
    private String descriptionRu;

    @Column(name = "description_eng", columnDefinition = "TEXT")
    private String descriptionEng;
}
