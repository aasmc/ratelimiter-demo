CREATE TABLE menu_items(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

create table user_ratelimiter(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY not null,
    user_id text not null,
    create_dt timestamp with time zone not null,
    update_dt timestamp with time zone
);

create unique index i_user_ratelimiter_user_id on user_ratelimiter(user_id);
