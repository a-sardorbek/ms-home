package com.system.uz.rest.model.admin.frequent;

import com.system.uz.enums.InfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentUpdateReq {
    @NotBlank(message = "frequent id is mandatory")
    private String frequentId;

    @NotBlank(message = "question_uz is mandatory")
    private String questionUz;

    @NotBlank(message = "question_ru is mandatory")
    private String questionRu;

    @NotBlank(message = "question_eng is mandatory")
    private String questionEng;

    @NotBlank(message = "answerUz is mandatory")
    private String answerUz;

    @NotBlank(message = "answerRu is mandatory")
    private String answerRu;

    @NotBlank(message = "answerEng is mandatory")
    private String answerEng;

    @NotNull(message = "type is mandatory")
    private InfoType type;
}
