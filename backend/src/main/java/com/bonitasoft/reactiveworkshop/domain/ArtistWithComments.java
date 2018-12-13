package com.bonitasoft.reactiveworkshop.domain;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@Builder
public class ArtistWithComments {

    private final String artistId;
    private final String artistName;
    private final String genre;
    private final List<Comment> comments;

    @Getter
    @Builder
    @Data
    public static class Comment {
        private final String comment;
        private final String userName;
    }

}
