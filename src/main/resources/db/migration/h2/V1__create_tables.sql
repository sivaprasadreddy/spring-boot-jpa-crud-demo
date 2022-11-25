create table bookmarks
(
    id         bigserial     not null,
    url        varchar(1024) not null,
    title      varchar(1024),
    created_at timestamp,
    primary key (id)
);

