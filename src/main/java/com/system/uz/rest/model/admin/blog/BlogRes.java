package com.system.uz.rest.model.admin.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRes {

    private String blogId;

    private String photoId;

    private String titleUz;

    private String titleRu;

    private String titleEng;

    private String descriptionUz;

    private String descriptionRu;

    private String descriptionEng;
}
