package com.system.uz.rest.service;

import com.system.uz.enums.InfoType;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.FrequentInfo;
import com.system.uz.rest.model.admin.client.ClientRes;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.model.admin.frequent.FrequentCreateReq;
import com.system.uz.rest.model.admin.frequent.FrequentRes;
import com.system.uz.rest.model.admin.frequent.FrequentUpdateReq;
import com.system.uz.rest.model.frequent.FrequentShortWhiteRes;
import com.system.uz.rest.repository.FrequentInfoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FrequentInfoService {

    private final FrequentInfoRepository frequentInfoRepository;

    public void create(FrequentCreateReq req) {
        FrequentInfo frequent = new FrequentInfo();
        frequent.setInfoId(Utils.generateToken());
        frequent.setQuestionUz(req.getQuestionUz());
        frequent.setQuestionRu(req.getQuestionRu());
        frequent.setQuestionEng(req.getQuestionEng());
        frequent.setAnswerUz(req.getAnswerUz());
        frequent.setAnswerRu(req.getAnswerRu());
        frequent.setAnswerEng(req.getAnswerEng());
        frequent.setType(req.getType());
        frequentInfoRepository.save(frequent);
    }

    public void update(FrequentUpdateReq req) {
        Optional<FrequentInfo> optionalFrequentInfo = frequentInfoRepository.findByInfoId(req.getFrequentId());
        if (optionalFrequentInfo.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        FrequentInfo frequent = optionalFrequentInfo.get();
        frequent.setQuestionUz(req.getQuestionUz());
        frequent.setQuestionRu(req.getQuestionRu());
        frequent.setQuestionEng(req.getQuestionEng());
        frequent.setAnswerUz(req.getAnswerUz());
        frequent.setAnswerRu(req.getAnswerRu());
        frequent.setAnswerEng(req.getAnswerEng());
        frequent.setType(req.getType());
        frequentInfoRepository.save(frequent);
    }

    public ResponseEntity<PagingResponse<FrequentRes>> getList(InfoType type, Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<FrequentInfo> frequentInfos;
        frequentInfos = frequentInfoRepository.findAllByType(Objects.nonNull(type) ? type : InfoType.MATERIAL, pageable);
        List<FrequentRes> frequentResList = new ArrayList<>();
        for (FrequentInfo frequentInfo : frequentInfos.getContent()) {
            frequentResList.add(new FrequentRes(
                    frequentInfo.getInfoId(),
                    frequentInfo.getQuestionUz(),
                    frequentInfo.getQuestionRu(),
                    frequentInfo.getQuestionEng(),
                    frequentInfo.getAnswerUz(),
                    frequentInfo.getAnswerRu(),
                    frequentInfo.getAnswerEng(),
                    frequentInfo.getType()
            ));
        }

        PagingResponse<FrequentRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(frequentResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), frequentInfos.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }

    public ResponseEntity<FrequentRes> getById(String frequentId) {
        Optional<FrequentInfo> optionalFrequentInfo = frequentInfoRepository.findByInfoId(frequentId);
        if (optionalFrequentInfo.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        FrequentInfo frequentInfo = optionalFrequentInfo.get();

        FrequentRes frequentRes = new FrequentRes(
                frequentInfo.getInfoId(),
                frequentInfo.getQuestionUz(),
                frequentInfo.getQuestionRu(),
                frequentInfo.getQuestionEng(),
                frequentInfo.getAnswerUz(),
                frequentInfo.getAnswerRu(),
                frequentInfo.getAnswerEng(),
                frequentInfo.getType()
        );

        return ResponseEntity.ok(frequentRes);
    }

    public void delete(String frequentId) {
        Optional<FrequentInfo> optionalFrequentInfo = frequentInfoRepository.findByInfoId(frequentId);
        if (optionalFrequentInfo.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        FrequentInfo frequentInfo = optionalFrequentInfo.get();
        frequentInfo.setDeletedAt(LocalDateTime.now());
        frequentInfoRepository.save(frequentInfo);
    }

    //==============================white=================================================

    public ResponseEntity<List<FrequentShortWhiteRes>> getWhiteList(InfoType type) {

        List<FrequentInfo> frequentInfos;
        frequentInfos = frequentInfoRepository.findAllByType(type);
        List<FrequentShortWhiteRes> frequentResList = new ArrayList<>();
        for (FrequentInfo frequentInfo : frequentInfos) {
            frequentResList.add(new FrequentShortWhiteRes(
                    frequentInfo.getInfoId(),
                    Utils.getLanguage(frequentInfo.getQuestionUz(), frequentInfo.getQuestionRu(), frequentInfo.getQuestionEng()),
                    Utils.getLanguage(frequentInfo.getAnswerUz(), frequentInfo.getAnswerRu(), frequentInfo.getQuestionEng()),
                    frequentInfo.getType()
            ));
        }

        return ResponseEntity.ok(frequentResList);
    }

    public ResponseEntity<FrequentShortWhiteRes> getWhiteById(String frequentId) {
        Optional<FrequentInfo> optionalFrequentInfo = frequentInfoRepository.findByInfoId(frequentId);
        if (optionalFrequentInfo.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        FrequentInfo frequentInfo = optionalFrequentInfo.get();

        FrequentShortWhiteRes frequentRes = new FrequentShortWhiteRes(
                frequentInfo.getInfoId(),
                Utils.getLanguage(frequentInfo.getQuestionUz(), frequentInfo.getQuestionRu(), frequentInfo.getQuestionEng()),
                Utils.getLanguage(frequentInfo.getAnswerUz(), frequentInfo.getAnswerRu(), frequentInfo.getQuestionEng()),
                frequentInfo.getType()
        );

        return ResponseEntity.ok(frequentRes);
    }
}
