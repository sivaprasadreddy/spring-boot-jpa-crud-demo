package com.sivalabs.bookmarks.adapter;

import com.sivalabs.bookmarks.ApplicationProperties;
import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import com.sivalabs.bookmarks.domain.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {
    private final JpaBookmarkRepository repo;
    private final BookmarkMapper bookmarkMapper;
    private final ApplicationProperties properties;

    @Override
    public PagedResult<Bookmark> findAll(int pageNo) {
        Pageable pageable = getPageable(pageNo);
        return new PagedResult<>(repo.findAllBookmarks(pageable));
    }

    @Override
    public Optional<Bookmark> findById(Long id) {
        return repo.findBookmarkById(id);
    }

    @Override
    public Bookmark save(Bookmark bookmark) {
        BookmarkEntity entity = bookmarkMapper.toEntity(bookmark);
        return bookmarkMapper.toModel(repo.save(entity));
    }

    @Override
    public Iterable<Bookmark> saveAll(Iterable<Bookmark> bookmarks) {
        List<BookmarkEntity> entities = StreamSupport.stream(bookmarks.spliterator(), false).map(bookmarkMapper::toEntity).toList();
        return repo.saveAll(entities).stream().map(bookmarkMapper::toModel).toList();
    }

    @Override
    public void deleteById(Long id) {
        repo.deleteBookmarkById(id);
    }

    @Override
    public long count() {
        return repo.count();
    }

    private Pageable getPageable(int pageNo) {
        int page = (pageNo > 0) ? pageNo - 1 : 0;
        return PageRequest.of(page, properties.pageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
