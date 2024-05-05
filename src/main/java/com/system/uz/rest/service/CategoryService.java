package com.system.uz.rest.service;

import com.system.uz.enums.Status;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.LocalDateTimeConverter;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Category;
import com.system.uz.rest.model.admin.blog.BlogRes;
import com.system.uz.rest.model.admin.category.CategoryActivationReq;
import com.system.uz.rest.model.admin.category.CategoryCreateReq;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.admin.category.CategoryUpdateReq;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void create(CategoryCreateReq req) {
        Category category = new Category();
        category.setCategoryId(Utils.generateToken());
        category.setTitleUz(req.getTitleUz());
        category.setTitleRu(req.getTitleRu());
        category.setTitleEng(req.getTitleEng());
        category.setDescriptionUz(req.getDescriptionUz());
        category.setDescriptionRu(req.getDescriptionRu());
        category.setDescriptionEng(req.getDescriptionEng());
        category.setStatus(Status.ACTIVE);
        categoryRepository.save(category);
    }

    public void update(CategoryUpdateReq req) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(req.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Category category = optionalCategory.get();
        category.setTitleUz(req.getTitleUz());
        category.setTitleRu(req.getTitleRu());
        category.setTitleEng(req.getTitleEng());
        category.setDescriptionUz(req.getDescriptionUz());
        category.setDescriptionRu(req.getDescriptionRu());
        category.setDescriptionEng(req.getDescriptionEng());

        categoryRepository.save(category);

    }

    public void activateDeactivate(CategoryActivationReq req) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(req.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        category.setStatus(req.isActive() ? Status.ACTIVE : Status.INACTIVE);
        categoryRepository.save(category);
    }

    public ResponseEntity<PagingResponse<CategoryRes>> getList(Status status, Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<Category> categories;
        if (Objects.nonNull(status)) {
            categories = categoryRepository.findAllByStatus(status, pageable);
        }else {
            categories = categoryRepository.findAll(pageable);
        }

        List<CategoryRes> categoryResList = new ArrayList<>();
        for (Category category : categories.getContent()) {
            categoryResList.add(new CategoryRes(
                    category.getCategoryId(),
                    category.getStatus(),
                    category.getTitleUz(),
                    category.getTitleRu(),
                    category.getTitleEng(),
                    category.getDescriptionUz(),
                    category.getDescriptionRu(),
                    category.getDescriptionEng()
            ));
        }

        PagingResponse<CategoryRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(categoryResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), categories.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }

    public ResponseEntity<CategoryRes> getById(String categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Category category = optionalCategory.get();

        CategoryRes categoryRes = new CategoryRes(
                category.getCategoryId(),
                category.getStatus(),
                category.getTitleUz(),
                category.getTitleRu(),
                category.getTitleEng(),
                category.getDescriptionUz(),
                category.getDescriptionRu(),
                category.getDescriptionEng());

        return ResponseEntity.ok(categoryRes);
    }

    public void delete(String categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(categoryId);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Category category = optionalCategory.get();
        category.setStatus(Status.INACTIVE);
        category.setDeletedAt(LocalDateTime.now());
        categoryRepository.save(category);
    }
}
