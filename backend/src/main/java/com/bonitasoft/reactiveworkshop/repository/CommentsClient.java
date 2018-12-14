package com.bonitasoft.reactiveworkshop.repository;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;

import java.util.List;

import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
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

    public List<Comment> getComments_resttemplate(String artistId) throws NotFoundException {
        log.info("Retrieving comments for artist {}", artistId);
        ResponseEntity<List<Comment>> response = restTemplate.exchange(
                format("%s/comments/%s/last10", baseUrl, artistId),
                GET,
                null,
                new ParameterizedTypeReference<List<Comment>>() {
                });
        List<Comment> comments = response.getBody();
        log.info("Found {} comments", comments.size());
        return comments;
    }

    public List<Comment> getComments(String artistId) throws NotFoundException {
        return getCommentsFlux(artistId)
                .collectList()
                .block()
                ;
    }

    public Flux<Comment> getCommentsFlux(String artistId) throws NotFoundException {
        log.info("Retrieving comments flux for artist {}", artistId);

        return this.webClient.get().uri("/comments/{artistId}/last10", artistId)
                .retrieve().bodyToFlux(Comment.class);
    }

}

//        org.springframework.dao.InvalidDataAccessApiUsageException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-2; nested exception is java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-2
// ...
//        io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:897)
//        Error has been observed by the following operator(s):
//        |_      Mono.flatMap ? org.springframework.web.reactive.result.method.InvocableHandlerMethod.invoke(InvocableHandlerMethod.java:135)
//        |_      Mono.defer ? org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.handle(RequestMappingHandlerAdapter.java:199)
//        |_      Mono.then ? org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.handle(RequestMappingHandlerAdapter.java:199)
//        |_      Mono.doOnNext ? org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.handle(RequestMappingHandlerAdapter.java:200)
//        |_      Mono.doOnNext ? org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.handle(RequestMappingHandlerAdapter.java:201)
//
//        Caused by: java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-2
//        at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:77) ~[reactor-core-3.2.3.RELEASE.jar:3.2.3.RELEASE]
//        at reactor.core.publisher.Mono.block(Mono.java:1493) [reactor-core-3.2.3.RELEASE.jar:3.2.3.RELEASE]
//        at com.bonitasoft.reactiveworkshop.repository.CommentsClient.getComments(CommentsClient.java:54) ~[main/:na]
//        at com.bonitasoft.reactiveworkshop.repository.CommentsClient$$FastClassBySpringCGLIB$$fde174f7.invoke(<generated>) ~[main/:na]
//        at org.springframework.cglib.proxy.MethodProxy.invoke(MethodProxy.java:218) ~[spring-core-5.1.3.RELEASE.jar:5.1.3.RELEASE]
//        at org.springframework.aop.framework.CglibAopProxy$CglibMethodInvocation.invokeJoinpoint(CglibAopProxy.java:749) ~[spring-aop-5.1.3.RELEASE.jar:5.1.3.RELEASE]
