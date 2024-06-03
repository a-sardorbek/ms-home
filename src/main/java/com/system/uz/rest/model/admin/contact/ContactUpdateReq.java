package com.system.uz.rest.model.admin.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactUpdateReq {

    @NotBlank(message = "contact id is mandatory")
    private String contactId;

    @NotBlank(message = "firstPhone is mandatory")
    private String firstPhone;

    @NotBlank(message = "secondPhone is mandatory")
    private String secondPhone;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Main officeUz is mandatory")
    private String mainOfficeUz;

    @NotBlank(message = "Main officeRu is mandatory")
    private String mainOfficeRu;

    @NotBlank(message = "Main officeEng is mandatory")
    private String mainOfficeEng;

    @NotBlank(message = "Production officeUz  is mandatory")
    private String productionOfficeUz;

    @NotBlank(message = "Production officeRu  is mandatory")
    private String productionOfficeRu;

    @NotBlank(message = "Production officeEng  is mandatory")
    private String productionOfficeEng;

    @NotBlank(message = "Description uz is mandatory")
    private String descriptionUz;

    @NotBlank(message = "Description ru is mandatory")
    private String descriptionRu;

    @NotBlank(message = "Description eng is mandatory")
    private String descriptionEng;
}
