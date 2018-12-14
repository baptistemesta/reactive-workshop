package com.bonitasoft.reactiveworkshop.repository;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;

import java.util.List;

import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Repository
@Slf4j
public class CommentsClient {

    private final WebClient webClient;
    private final RestTemplate restTemplate;
    // TODO utiliser la prop de l'application
    private final String baseUrl = "http://localhost:3004";

    public CommentsClient(WebClient.Builder webClientBuilder, RestTemplateBuilder restTemplateBuilder) {
        // highly inspired from https://grokonez.com/spring-framework/spring-webflux/spring-webclient-spring-webflux-springboot-2
        this.webClient = webClientBuilder.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl(baseUrl).build();

        this.restTemplate = restTemplateBuilder.rootUri(baseUrl).build();
    }

    public List<Comment> getComments(String artistId) throws NotFoundException {
        log.info("Retrieving comments for artist {}", artistId);
        ResponseEntity<List<Comment>> response = restTemplate.exchange(
                format("%s/comments/%s/last10", baseUrl, artistId),
                GET,
                null,
                new ParameterizedTypeReference<List<Comment>>(){});
        List<Comment> comments = response.getBody();
        log.info("Found {} comments", comments.size());
        return comments;
    }

    public Flux<Comment> getCommentsFlux(String artistId) throws NotFoundException {
        log.info("Retrieving comments flux for artist {}", artistId);

        return this.webClient.get().uri("/comments/{artistId}/last10", artistId)
                .retrieve().bodyToFlux(Comment.class)
                ;
    }

}
