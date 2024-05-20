package com.system.uz.rest.model.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactWhiteRes {
    private String contactId;
    private String firstPhone;
    private String secondPhone;
    private String email;
    private String mainOffice;
    private String productionOffice;
    private String description;
}
