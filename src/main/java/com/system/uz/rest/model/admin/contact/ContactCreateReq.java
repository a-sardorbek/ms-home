package com.system.uz.rest.model.admin.contact;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactCreateReq {

    @NotBlank(message = "firstPhone is mandatory")
    private String firstPhone;

    @NotBlank(message = "secondPhone is mandatory")
    private String secondPhone;

    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Main office is mandatory")
    private String mainOffice;

    @NotBlank(message = "Production office  is mandatory")
    private String productionOffice;

    @NotBlank(message = "Description uz is mandatory")
    private String descriptionUz;

    @NotBlank(message = "Description ru is mandatory")
    private String descriptionRu;

    @NotBlank(message = "Description eng is mandatory")
    private String descriptionEng;

}
