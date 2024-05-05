package com.system.uz.rest.model.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogWhiteShortRes {
    private String blogId;

    private String photoId;

    private String title;

    private String description;
}
