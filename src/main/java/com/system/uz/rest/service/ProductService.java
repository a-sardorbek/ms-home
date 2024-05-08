package com.system.uz.rest.service;

import com.system.uz.enums.BucketFolder;
import com.system.uz.enums.ImageType;
import com.system.uz.enums.Lang;
import com.system.uz.exceptions.BadRequestException;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Category;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.domain.Product;
import com.system.uz.rest.model.admin.category.CategoryRes;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.model.image.ImageInfo;
import com.system.uz.rest.model.image.ImageRes;
import com.system.uz.rest.model.admin.product.*;
import com.system.uz.rest.model.image.PlanImage;
import com.system.uz.rest.model.image.ProductImage;
import com.system.uz.rest.model.product.ProductWhiteRes;
import com.system.uz.rest.model.product.ProductWhiteShortRes;
import com.system.uz.rest.repository.CategoryRepository;
import com.system.uz.rest.repository.ImageRepository;
import com.system.uz.rest.repository.ProductRepository;
import com.system.uz.rest.service.component.MinioService;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final MinioService minioService;

    public void create(ProductCreateReq req) {
        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(req.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Product product = new Product();
        product.setProductId(Utils.generateToken());
        product.setSize(req.getSize());
        product.setTitleUz(req.getTitleUz());
        product.setTitleRu(req.getTitleRu());
        product.setTitleEng(req.getTitleEng());
        product.setPlanUz(req.getPlanUz());
        product.setPlanRu(req.getPlanRu());
        product.setPlanEng(req.getPlanEng());
        product.setCategoryId(req.getCategoryId());

        productRepository.save(product);
    }

    public void update(ProductUpdateReq req) {
        Optional<Product> optionalProduct = productRepository.findByProductId(req.getProductId());
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Optional<Category> optionalCategory = categoryRepository.findByCategoryId(req.getCategoryId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Product product = optionalProduct.get();
        product.setSize(req.getSize());
        product.setTitleUz(req.getTitleUz());
        product.setTitleRu(req.getTitleRu());
        product.setTitleEng(req.getTitleEng());
        product.setPlanUz(req.getPlanUz());
        product.setPlanRu(req.getPlanRu());
        product.setPlanEng(req.getPlanEng());
        product.setCategoryId(req.getCategoryId());

        productRepository.save(product);
    }

    public ResponseEntity<ProductRes> getById(String productId) {
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Product product = optionalProduct.get();
        CategoryRes categoryRes = getCategoryRes(product);

        List<ProductImage> imageProducts = new ArrayList<>();
        List<PlanImage> imagePlans = new ArrayList<>();

        imageRepository.findAllByProductId(product.getProductId()).forEach(image -> {
            switch (image.getType()) {
                case PLAN:
                    imagePlans.add(new PlanImage(image.getImageId(), image.getTitle()));
                    break;
                case PROJECT:
                    imageProducts.add(new ProductImage(image.getImageId()));
                    break;
            }
        });

        ProductRes productRes = new ProductRes();
        productRes.setProductId(productId);
        productRes.setSize(product.getSize());
        productRes.setTitleUz(product.getTitleUz());
        productRes.setTitleRu(product.getTitleRu());
        productRes.setTitleEng(product.getTitleEng());
        productRes.setPlanUz(product.getPlanUz());
        productRes.setPlanRu(product.getPlanRu());
        productRes.setPlanEng(product.getPlanEng());
        productRes.setCategory(categoryRes);
        productRes.setPhoto(new ImageRes(imageProducts, imagePlans));

        return ResponseEntity.ok(productRes);
    }

    public ResponseEntity<PagingResponse<ProductRes>> getList(String categoryId, Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<Product> products;
        if (Utils.isValidString(categoryId)) {
            products = productRepository.findAllByCategoryId(categoryId, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        List<ProductImage> imageProducts = new ArrayList<>();
        List<PlanImage> imagePlans = new ArrayList<>();

        List<ProductRes> productResList = products.getContent().stream()
                .map(product -> {
                    CategoryRes categoryRes = getCategoryRes(product);

                    imageRepository.findAllByProductId(product.getProductId()).forEach(image -> {
                        switch (image.getType()) {
                            case PLAN:
                                imagePlans.add(new PlanImage(image.getImageId(), image.getTitle()));
                                break;
                            case PROJECT:
                                imageProducts.add(new ProductImage(image.getImageId()));
                                break;
                        }
                    });

                    ProductRes productRes = new ProductRes();
                    productRes.setProductId(product.getProductId());
                    productRes.setSize(product.getSize());
                    productRes.setTitleUz(product.getTitleUz());
                    productRes.setTitleRu(product.getTitleRu());
                    productRes.setTitleEng(product.getTitleEng());
                    productRes.setPlanUz(product.getPlanUz());
                    productRes.setPlanRu(product.getPlanRu());
                    productRes.setPlanEng(product.getPlanEng());
                    productRes.setCategory(categoryRes);
                    productRes.setPhoto(new ImageRes(imageProducts, imagePlans));
                    return productRes;
                })
                .collect(Collectors.toList());

//        List<ProductRes> productResList = new ArrayList<>();
//        List<String> imageProducts = new ArrayList<>();
//        List<String> imagePlans = new ArrayList<>();
//
//        for (Product product : products.getContent()) {
//            CategoryRes categoryRes = getCategoryRes(product);
//
//            List<Image> images = imageRepository.findAllByProductId(product);
//            for (Image image : images) {
//                switch (image.getType()) {
//                    case PLAN:
//                        imagePlans.add(image.getImageId());
//                        break;
//                    case PROJECT:
//                        imageProducts.add(image.getImageId());
//                        break;
//                }
//            }
//
//            ProductRes productRes = new ProductRes();
//            productRes.setProductId(product.getProductId());
//            productRes.setSize(product.getSize());
//            productRes.setTitleUz(product.getTitleUz());
//            productRes.setTitleRu(product.getTitleRu());
//            productRes.setTitleEng(product.getTitleEng());
//            productRes.setPlanUz(product.getPlanUz());
//            productRes.setPlanRu(product.getPlanRu());
//            productRes.setPlanEng(product.getPlanEng());
//            productRes.setCategory(categoryRes);
//            productRes.setPhoto(new ImageRes(imageProducts, imagePlans));
//            productResList.add(productRes);
//        }

        PagingResponse<ProductRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(productResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), products.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }

    public void delete(String productId){
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Product product = optionalProduct.get();
        product.setDeletedAt(LocalDateTime.now());
        productRepository.save(product);
    }

    public void addImage(ProductAddImageReq req){
        Optional<Product> optionalProduct = productRepository.findByProductId(req.getProductId());
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        if(req.getType().equals(ImageType.BLOG)){
            throw new BadRequestException(MessageKey.BLOG_TYPE_CANNOT_BE_APPLIED);
        }

        if(req.getType().equals(ImageType.PLAN)){
            if(!Utils.isValidString(req.getTitle())){
                throw new BadRequestException(MessageKey.TITLE_MANDATORY_FOR_PLAN_TYPE);
            }
        }

        ImageInfo imageInfo = minioService.uploadImage(req.getPhoto(), BucketFolder.PRODUCT);

        Image image = new Image();
        image.setImageId(Utils.generateToken());
        image.setImageOriginalName(imageInfo.getName());
        image.setFullPath(imageInfo.getPath());
        image.setContentType(imageInfo.getContent());
        image.setType(req.getType());
        image.setProductId(req.getProductId());

        if(Utils.isValidString(req.getTitle())){
            image.setTitle(req.getTitle());
        }

        imageRepository.save(image);
    }

    public void updateImage(ProductUpdateImageReq req){
        Optional<Image> optionalImage = imageRepository.findByProductIdAndImageId(req.getProductId(), req.getPhotoId());
        if (optionalImage.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Image image = optionalImage.get();
        String oldPath = image.getFullPath();

        if(req.getType().equals(ImageType.PLAN)){
            if(!Utils.isValidString(req.getTitle())){
                throw new BadRequestException(MessageKey.TITLE_MANDATORY_FOR_PLAN_TYPE);
            }
        }

        ImageInfo imageInfo = minioService.uploadImage(req.getPhoto(), BucketFolder.PRODUCT);

        image.setImageOriginalName(imageInfo.getName());
        image.setFullPath(imageInfo.getPath());
        image.setContentType(imageInfo.getContent());
        image.setType(req.getType());

        if(Utils.isValidString(req.getTitle())){
            image.setTitle(req.getTitle());
        }

        imageRepository.save(image);

        minioService.deleteImage(oldPath);
    }

    public void deleteImage(String photoId, String productId){
        Optional<Image> optionalImage = imageRepository.findByProductIdAndImageId(productId, photoId);
        if (optionalImage.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Image image = optionalImage.get();
        image.setDeletedAt(LocalDateTime.now());
        imageRepository.save(image);

        minioService.deleteImage(image.getFullPath());
    }

    private CategoryRes getCategoryRes(Product product) {
        CategoryRes categoryRes = null;

        if (Objects.nonNull(product.getCategory())) {
            Category category = product.getCategory();
            categoryRes = new CategoryRes(
                    category.getCategoryId(),
                    category.getStatus(),
                    category.getTitleUz(),
                    category.getTitleRu(),
                    category.getTitleEng(),
                    category.getDescriptionUz(),
                    category.getDescriptionRu(),
                    category.getDescriptionEng()
            );
        }
        return categoryRes;
    }

    //======================WHITE====================================================================

    public ResponseEntity<List<ProductWhiteShortRes>> getShortWhiteList(String categoryId) {

        List<Product> products;
        if (Utils.isValidString(categoryId)) {
            products = productRepository.findTop50ByCategoryIdOrderByIdDesc(categoryId);
        } else {
            products = productRepository.findTop50ByOrderByIdDesc();
        }

        List<ProductWhiteShortRes> productResList = products.stream()
                .map(product -> {

                    List<Image> images = imageRepository.findAllByProductIdAndType(product.getProductId(), ImageType.PROJECT);

                    ProductWhiteShortRes productRes = new ProductWhiteShortRes();
                    productRes.setProductId(product.getProductId());
                    productRes.setSize(product.getSize());
                    productRes.setTitle(Utils.getLanguage(product.getTitleUz(), product.getTitleRu(), product.getTitleEng()));
                    productRes.setPhoto(images.isEmpty() ? "" : images.get(0).getImageId());
                    return productRes;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(productResList);
    }

    public ResponseEntity<ProductWhiteRes> getWhiteById(String productId) {

        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (optionalProduct.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Product product = optionalProduct.get();
        List<ProductImage> imageProducts = new ArrayList<>();
        List<PlanImage> imagePlans = new ArrayList<>();

        imageRepository.findAllByProductId(product.getProductId()).forEach(image -> {
            switch (image.getType()) {
                case PLAN:
                    imagePlans.add(new PlanImage(image.getImageId(), image.getTitle()));
                    break;
                case PROJECT:
                    imageProducts.add(new ProductImage(image.getImageId()));
                    break;
            }
        });

        ProductWhiteRes productRes = new ProductWhiteRes();
        productRes.setProductId(productId);
        productRes.setSize(product.getSize());
        productRes.setTitle(Utils.getLanguage(product.getTitleUz(), product.getTitleRu(), product.getTitleEng()));
        productRes.setPlanDescription(Utils.getLanguage(product.getPlanUz(), product.getPlanRu(), product.getPlanEng()));
        productRes.setPhoto(new ImageRes(imageProducts, imagePlans));

        return ResponseEntity.ok(productRes);
    }
}
