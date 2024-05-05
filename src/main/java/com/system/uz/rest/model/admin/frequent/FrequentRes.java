package com.system.uz.rest.model.admin.frequent;

import com.system.uz.enums.InfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentRes {
    private String frequentId;

    private String questionUz;

    private String questionRu;

    private String questionEng;

    private String answerUz;

    private String answerRu;

    private String answerEng;

    private InfoType type;
}
