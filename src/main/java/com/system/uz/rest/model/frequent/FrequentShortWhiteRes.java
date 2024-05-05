package com.system.uz.rest.model.frequent;

import com.system.uz.enums.InfoType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FrequentShortWhiteRes {
    private String frequentId;

    private String question;

    private String answer;

    private InfoType type;
}
