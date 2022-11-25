package com.sivalabs.bookmarks;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:15-alpine:///dbname"
})
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkRepository repo;

    @ParameterizedTest
    @CsvSource({
            "1,15,2,1,true,false,true,false",
            "2,15,2,2,false,true,false,true"
    })
    void shouldFetchBookmarksByPageNumber(
            int pageNo,
            int totalElements,
            int totalPages,
            int pageNumber,
            boolean isFirst,
            boolean isLast,
            boolean hasNext,
            boolean hasPrevious)
            throws Exception {
        this.mockMvc
                .perform(get("/api/bookmarks?page=" + pageNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", equalTo(totalElements)))
                .andExpect(jsonPath("$.totalPages", equalTo(totalPages)))
                .andExpect(jsonPath("$.pageNumber", equalTo(pageNumber)))
                .andExpect(jsonPath("$.isFirst", equalTo(isFirst)))
                .andExpect(jsonPath("$.isLast", equalTo(isLast)))
                .andExpect(jsonPath("$.hasNext", equalTo(hasNext)))
                .andExpect(jsonPath("$.hasPrevious", equalTo(hasPrevious)));
    }

    @Test
    void shouldGetBookmarkById() throws Exception {
        Bookmark bookmark = new Bookmark(
                null,
                "New Bookmark",
                "https://my-new-bookmark.com",
                Instant.now());
        Bookmark savedBookmark = repo.save(bookmark);
        this.mockMvc
                .perform(get("/api/bookmarks/{id}", savedBookmark.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(savedBookmark.id()), Long.class))
                .andExpect(jsonPath("$.title", equalTo(savedBookmark.title())))
                .andExpect(jsonPath("$.url", equalTo(savedBookmark.url())));
    }

    @Test
    void shouldCreateBookmarkSuccessfully() throws Exception {
        this.mockMvc
                .perform(
                        post("/api/bookmarks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "title": "SivaLabs Blog",
                                                    "url": "https://sivalabs.in"
                                                }
                                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", equalTo("SivaLabs Blog")))
                .andExpect(jsonPath("$.url", equalTo("https://sivalabs.in")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{ \"title\": \"SivaLabs Blog\" }", "{ \"url\": \"https://sivalabs.in\" }"})
    void shouldFailToCreateBookmarkWhenTitleOrUrlIsNotPresent(String payload) throws Exception {
        this.mockMvc.perform(post("/api/bookmarks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteBookmarkById() throws Exception {
        Bookmark bookmark = new Bookmark(
                null,
                "New Bookmark",
                "https://my-new-bookmark.com",
                Instant.now());
        Bookmark savedBookmark = repo.save(bookmark);
        assertThat(repo.findById(savedBookmark.id())).isPresent();
        this.mockMvc
                .perform(delete("/api/bookmarks/{id}", savedBookmark.id()))
                .andExpect(status().isNoContent());
        assertThat(repo.findById(savedBookmark.id())).isEmpty();
    }
}
