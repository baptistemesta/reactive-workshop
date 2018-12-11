package com.bonitasoft.reactiveworkshop.infra;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final ArtistRepository repository;

    public DataInitializer(ArtistRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        repository.deleteAll();
        repository.insert(Artist.builder().id("blink_182").name("Blink 182").build());
        repository.insert(Artist.builder().id("green_day").name("Green day").build());
//                .deleteAll()
//                .thenMany(
//                        Flux
//                                .just("A", "B", "C", "D")
//                                .map(name -> new Profile(UUID.randomUUID().toString(), name + "@email.com"))
//                                .flatMap(repository::save)
//                )
//                .thenMany(repository.findAll())
//                .subscribe(profile -> log.info("saving " + profile.toString()));
    }
}
