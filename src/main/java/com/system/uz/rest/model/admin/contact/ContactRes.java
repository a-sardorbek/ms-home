package com.system.uz.rest.model.admin.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRes {
    private String contactId;
    private String firstPhone;
    private String secondPhone;
    private String email;
    private String mainOffice;
    private String productionOffice;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEng;
}
