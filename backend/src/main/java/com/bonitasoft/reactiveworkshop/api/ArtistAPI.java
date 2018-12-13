package com.bonitasoft.reactiveworkshop.api;

import java.util.List;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments.ArtistWithCommentsBuilder;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArtistAPI {


    private ArtistRepository artistRepository;

    public ArtistAPI(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping("/artist/{id}")
    public Artist findById(@PathVariable String id) throws NotFoundException {
        return artistRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/artist/{id}/comments")
    public ArtistWithComments findCommentsByArtistId(@PathVariable String id) throws NotFoundException {
        Artist artist = findById(id);

        ArtistWithCommentsBuilder artistWithCommentsBuilder = ArtistWithComments.builder().artistId(artist.getId())
                .artistName(artist.getName())
                .genre(artist.getGenre());

        return artistWithCommentsBuilder.build();
    }

    @GetMapping("/artists")
    public List<Artist> findAll() throws NotFoundException {
        return artistRepository.findAll();
    }
}
