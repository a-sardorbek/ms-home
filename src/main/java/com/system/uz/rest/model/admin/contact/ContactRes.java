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
    private String mainOfficeUz;
    private String mainOfficeRu;
    private String mainOfficeEng;
    private String productionOfficeUz;
    private String productionOfficeRu;
    private String productionOfficeEng;
    private String descriptionUz;
    private String descriptionRu;
    private String descriptionEng;
}
