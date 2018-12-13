package com.bonitasoft.reactiveworkshop.api;

import static java.util.Arrays.asList;
import static java.util.Optional.of;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments;
import com.bonitasoft.reactiveworkshop.domain.ArtistWithComments.Comment;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import com.bonitasoft.reactiveworkshop.repository.CommentsClient;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


public class ArtistAPITest {

    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();
    @Rule
    public final JUnitSoftAssertions softly = new JUnitSoftAssertions();

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private CommentsClient commentsClient;

    @InjectMocks
    private ArtistAPI artistAPI;

    @Test(expected = NotFoundException.class)
    public void should_not_find_artist_with_comments_when_the_artist_is_unknown() throws NotFoundException {
        artistAPI.findCommentsByArtistId("135");
    }

    @Test
    public void should_find_artist_with_comments() throws NotFoundException {
        doReturn(of(Artist.builder().id("135").name("Mike").genre("Pop").build())).when(artistRepository).findById("135");
        doReturn(asList(comment("user0", "comment0"), comment("user1", "comment1"))).when(commentsClient).getComments("135");
        ArtistWithComments artist = artistAPI.findCommentsByArtistId("135");
        softly.assertThat(artist.getArtistId()).isEqualTo("135");
        softly.assertThat(artist.getArtistName()).isEqualTo("Mike");
        softly.assertThat(artist.getGenre()).isEqualTo("Pop");

        List<Comment> comments = artist.getComments();
        softly.assertThat(comments).extracting(Comment::getComment).containsExactly("comment0", "comment1");
        softly.assertThat(comments).extracting(Comment::getUserName).containsExactly("user0", "user1");
    }

    @Test
    public void should_find_artist_without_comments() throws NotFoundException {
        doReturn(of(Artist.builder().id("135").name("John").build())).when(artistRepository).findById("135");
        ArtistWithComments artist = artistAPI.findCommentsByArtistId("135");
        softly.assertThat(artist.getArtistId()).isEqualTo("135");

        softly.assertThat(artist.getComments()).isEmpty();
    }

    // ======================================================

    private static Comment comment(String userName, String comment) {
        return Comment.builder().userName(userName).comment(comment).build();
    }

}