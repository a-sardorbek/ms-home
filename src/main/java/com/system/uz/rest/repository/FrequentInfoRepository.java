package com.system.uz.rest.repository;

import com.system.uz.enums.InfoType;
import com.system.uz.rest.domain.FrequentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FrequentInfoRepository extends JpaRepository<FrequentInfo, Long> {
    Optional<FrequentInfo> findByInfoId(String frequentId);

    Page<FrequentInfo> findAllByType(InfoType type, Pageable pageable);
    List<FrequentInfo> findAllByType(InfoType type);

    List<FrequentInfo> findTop10ByTypeOrderByIdDesc(InfoType infoType);

    long countByType(InfoType type);
}
