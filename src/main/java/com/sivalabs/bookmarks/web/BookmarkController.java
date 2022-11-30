package com.sivalabs.bookmarks.web;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkNotFoundException;
import com.sivalabs.bookmarks.domain.BookmarkService;
import com.sivalabs.bookmarks.domain.PagedResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService service;

    @GetMapping
    public PagedResult<Bookmark> getBookmarks(@RequestParam(name = "page", defaultValue = "1") Integer page) {
        return service.getBookmarks(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable Long id) {
        return service.getBookmarkById(id)
                .map(ResponseEntity::ok)
                //.orElseGet(() -> ResponseEntity.notFound().build());
                .orElseThrow(() -> new BookmarkNotFoundException(id));
    }

    @PostMapping
    public ResponseEntity<Bookmark> save(@Valid @RequestBody Bookmark payload) {
        Bookmark bookmark = new Bookmark(null, payload.title(), payload.url(), Instant.now());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(bookmark));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
