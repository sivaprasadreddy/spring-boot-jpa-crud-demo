package com.sivalabs.bookmarks.adapter;

import com.sivalabs.bookmarks.domain.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    public BookmarkEntity toEntity(Bookmark bookmark) {
        BookmarkEntity entity = new BookmarkEntity();
        entity.setId(bookmark.id());
        entity.setTitle(bookmark.title());
        entity.setUrl(bookmark.url());
        entity.setCreatedAt(bookmark.createdAt());
        return entity;
    }

    public Bookmark toModel(BookmarkEntity entity) {
        return new Bookmark(entity.getId(),entity.getTitle(),entity.getUrl(),entity.getCreatedAt());
    }
}
