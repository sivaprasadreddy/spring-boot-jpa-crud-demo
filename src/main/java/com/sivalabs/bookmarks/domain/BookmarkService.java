package com.sivalabs.bookmarks.domain;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final BookmarkRepository repo;

    public PagedResult<Bookmark> getBookmarks(int pageNo) {
        return repo.findAll(pageNo);
    }

    public Optional<Bookmark> getBookmarkById(Long id) {
        return repo.findById(id);
    }

    public Bookmark save(Bookmark bookmark) {
        return repo.save(bookmark);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
