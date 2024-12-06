CREATE TABLE items(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    user_name TEXT NOT NULL
);

create index i_items_user on items(user_name);

create table user_ratelimiter(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY not null,
    user_id text not null,
    create_dt timestamp with time zone not null,
    update_dt timestamp with time zone
);

create unique index i_user_ratelimiter_user_id on user_ratelimiter(user_id);
