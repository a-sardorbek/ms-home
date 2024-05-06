package com.system.uz.rest.domain;

import com.system.uz.base.BaseEntity;
import com.system.uz.enums.InfoType;
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
@Table(name = "frequent_infos")
public class FrequentInfo extends BaseEntity {

    @Column(name = "info_id", nullable = false, unique = true)
    private String infoId;

    @Column(name = "question_uz", columnDefinition = "TEXT")
    private String questionUz;

    @Column(name = "question_ru", columnDefinition = "TEXT")
    private String questionRu;

    @Column(name = "question_eng", columnDefinition = "TEXT")
    private String questionEng;

    @Column(name = "answer_uz", columnDefinition = "TEXT")
    private String answerUz;

    @Column(name = "answer_ru", columnDefinition = "TEXT")
    private String answerRu;

    @Column(name = "answer_eng", columnDefinition = "TEXT")
    private String answerEng;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private InfoType type;

}
