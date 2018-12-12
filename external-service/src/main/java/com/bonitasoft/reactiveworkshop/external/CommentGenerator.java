package com.bonitasoft.reactiveworkshop.external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import com.devskiller.jfairy.Fairy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CommentGenerator {

    private static final List<String> ARTISTS = parseArtists();

    private static final Fairy fairy = Fairy.create();

    public Mono<Comment> generateComment(String artistId) {
        return Mono.fromSupplier(() -> {
            String username = fairy.person().getUsername();
            String comment = fairy.textProducer().latinSentence();
            return new Comment(artistId, username, comment);
        });
    }

    public Mono<Comment> generateComment() {
        return Mono.fromSupplier(() -> {
            String artist = ARTISTS.get(Integer.valueOf(Math.round(Math.random() * ARTISTS.size()) + ""));
            String username = fairy.person().getUsername();
            String comment = fairy.textProducer().latinSentence();
            return new Comment(artist, username, comment);
        });
    }

    public static List<String> parseArtists() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(CommentGenerator.class.getResourceAsStream("/artists_only")))) {
            return reader.lines().map(name -> {
                try {
                    return DigestUtils.md5DigestAsHex(name.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalStateException("Unable to parse artists file");
                }
            }).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse artists file");
        }
    }
}
