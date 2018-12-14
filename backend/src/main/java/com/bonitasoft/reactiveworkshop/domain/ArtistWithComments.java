package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class ArtistWithComments {

    private final String artistId;
    private final String artistName;
    private final String genre;
    private final List<Comment> comments;

    @Getter
    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comment {
        private String comment;
        private String userName;
    }

}
