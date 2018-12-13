package com.bonitasoft.reactiveworkshop.repository;

import java.util.List;

import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class CommentsClient {

    public List<Comment> getComments(String artistId) throws NotFoundException {

        return null;
    }

}
