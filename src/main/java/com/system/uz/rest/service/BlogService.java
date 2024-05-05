package com.system.uz.rest.service;

import com.system.uz.enums.BucketFolder;
import com.system.uz.enums.ImageType;
import com.system.uz.exceptions.NotFoundException;
import com.system.uz.global.GlobalVar;
import com.system.uz.global.MessageKey;
import com.system.uz.global.PagingResponse;
import com.system.uz.global.Utils;
import com.system.uz.rest.domain.Blog;
import com.system.uz.rest.domain.Image;
import com.system.uz.rest.model.admin.blog.BlogCreateReq;
import com.system.uz.rest.model.admin.blog.BlogRes;
import com.system.uz.rest.model.admin.blog.BlogUpdateReq;
import com.system.uz.rest.model.admin.criteria.PageSize;
import com.system.uz.rest.model.blog.BlogWhiteShortRes;
import com.system.uz.rest.model.image.ImageInfo;
import com.system.uz.rest.repository.BlogRepository;
import com.system.uz.rest.repository.ImageRepository;
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
import java.util.Optional;

@Service
@AllArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final MinioService minioService;
    private final ImageRepository imageRepository;

    public void create(BlogCreateReq req) {
        Blog blog = new Blog();
        blog.setBlogId(Utils.generateToken());
        blog.setTitleUz(req.getTitleUz());
        blog.setTitleRu(req.getTitleRu());
        blog.setTitleEng(req.getTitleEng());
        blog.setDescriptionUz(req.getDescriptionUz());
        blog.setDescriptionRu(req.getDescriptionRu());
        blog.setDescriptionEng(req.getDescriptionEng());

        ImageInfo imageInfo = minioService.uploadImage(req.getPhoto(), BucketFolder.BLOG);

        Blog newBlog = blogRepository.save(blog);

        Image image = new Image();
        image.setImageId(Utils.generateToken());
        image.setBlogId(newBlog.getBlogId());
        image.setImageOriginalName(imageInfo.getName());
        image.setContentType(imageInfo.getContent());
        image.setType(ImageType.BLOG);
        image.setFullPath(imageInfo.getPath());
        imageRepository.save(image);
    }

    public void update(BlogUpdateReq req) {
        Optional<Blog> optionalCategory = blogRepository.findByBlogId(req.getBlogId());
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }

        Blog blog = optionalCategory.get();
        blog.setTitleUz(req.getTitleUz());
        blog.setTitleRu(req.getTitleRu());
        blog.setTitleEng(req.getTitleEng());
        blog.setDescriptionUz(req.getDescriptionUz());
        blog.setDescriptionRu(req.getDescriptionRu());
        blog.setDescriptionEng(req.getDescriptionEng());


        if (Utils.isValidString(req.getPhoto())) {

            Optional<Image> optionalImage = imageRepository.findByBlogId(blog.getBlogId());
            ImageInfo imageInfo = minioService.uploadImage(req.getPhoto(), BucketFolder.BLOG);
            Image image;
            if (optionalImage.isPresent()) {
                image = optionalImage.get();
                String olImage = image.getFullPath();
                image.setImageOriginalName(imageInfo.getName());
                image.setContentType(imageInfo.getContent());
                image.setFullPath(imageInfo.getPath());
                image.setType(ImageType.BLOG);
                minioService.deleteImage(olImage);
            } else {
                image = new Image();
                image.setImageId(Utils.generateToken());
                image.setBlogId(blog.getBlogId());
                image.setImageOriginalName(imageInfo.getName());
                image.setContentType(imageInfo.getContent());
                image.setFullPath(imageInfo.getPath());
                image.setType(ImageType.BLOG);
            }
            imageRepository.save(image);
        }
        blogRepository.save(blog);
    }


    public ResponseEntity<PagingResponse<BlogRes>> getList(Integer page, Integer size) {
        PageSize pageSize = Utils.validatePageSize(page, size);
        Pageable pageable = PageRequest.of(pageSize.getPage(), pageSize.getSize(), Sort.by("id").descending());

        Page<Blog> blogs = blogRepository.findAll(pageable);
        List<BlogRes> blogResList = new ArrayList<>();
        for (Blog blog : blogs.getContent()) {

            blogResList.add(new BlogRes(
                    blog.getBlogId(),
                    getImage(blog.getBlogId()),
                    blog.getTitleUz(),
                    blog.getTitleRu(),
                    blog.getTitleEng(),
                    blog.getDescriptionUz(),
                    blog.getDescriptionRu(),
                    blog.getDescriptionEng()
            ));
        }

        PagingResponse<BlogRes> pagingResponse = new PagingResponse<>();
        pagingResponse.setContent(blogResList);
        pagingResponse.setPagingParams(pagingResponse, pageSize.getPage(), pageSize.getSize(), blogs.getTotalElements());

        return ResponseEntity.ok(pagingResponse);
    }

    public ResponseEntity<BlogRes> getById(String blogId) {
        Optional<Blog> optionalCategory = blogRepository.findByBlogId(blogId);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Blog blog = optionalCategory.get();

        BlogRes blogRes = new BlogRes(
                blog.getBlogId(),
                getImage(blogId),
                blog.getTitleUz(),
                blog.getTitleRu(),
                blog.getTitleEng(),
                blog.getDescriptionUz(),
                blog.getDescriptionRu(),
                blog.getDescriptionEng());

        return ResponseEntity.ok(blogRes);
    }

    public void delete(String blogId) {
        Optional<Blog> optionalCategory = blogRepository.findByBlogId(blogId);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Blog blog = optionalCategory.get();
        blog.setDeletedAt(LocalDateTime.now());
        blogRepository.save(blog);
    }

    private String getImage(String blogId){
        String photo = null;
        Optional<Image> optionalImage = imageRepository.findByBlogId(blogId);
        if(optionalImage.isPresent()){
            photo = optionalImage.get().getImageId();
        }
        return photo;
    }


    // ==========================white==============================================

    public ResponseEntity<List<BlogWhiteShortRes>> getWhiteList() {
        List<Blog> blogs = blogRepository.findTop30ByOrderByIdDesc();
        List<BlogWhiteShortRes> blogResList = new ArrayList<>();
        for (Blog blog : blogs) {

            blogResList.add(new BlogWhiteShortRes(
                    blog.getBlogId(),
                    getImage(blog.getBlogId()),
                    Utils.getLanguage(blog.getTitleUz(), blog.getTitleRu(), blog.getTitleEng()),
                    Utils.getLanguage(blog.getDescriptionUz(), blog.getDescriptionRu(), blog.getDescriptionEng())
            ));
        }

        return ResponseEntity.ok(blogResList);
    }

    public ResponseEntity<BlogWhiteShortRes> getWhiteById(String blogId) {
        Optional<Blog> optionalCategory = blogRepository.findByBlogId(blogId);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(MessageKey.NOT_FOUND);
        }
        Blog blog = optionalCategory.get();


        BlogWhiteShortRes blogRes = new BlogWhiteShortRes(
                blog.getBlogId(),
                getImage(blog.getBlogId()),
                Utils.getLanguage(blog.getTitleUz(), blog.getTitleRu(), blog.getTitleEng()),
                Utils.getLanguage(blog.getDescriptionUz(), blog.getDescriptionRu(), blog.getDescriptionEng())
        );

        return ResponseEntity.ok(blogRes);
    }
}
