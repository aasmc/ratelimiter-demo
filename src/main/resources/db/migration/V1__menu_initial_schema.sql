CREATE TABLE menu_items(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL
);

create extension btree_gist;

create table user_ratelimiter(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY not null,
    user_id text not null,
    limiter_range tstzrange not null,
    allowed integer not null,
    create_dt timestamp with time zone not null,
    update_dt timestamp with time zone,
    constraint user_limiter_exclusion_constraint
        exclude using gist (user_id with =, limiter_range with &&)
);

CREATE OR REPLACE FUNCTION upsert_user_ratelimiter(
    p_user_id TEXT,
    p_limiter_range TSTZRANGE,
    p_allowed INTEGER,
    p_create_dt TIMESTAMPTZ,
    p_update_dt TIMESTAMPTZ
) RETURNS INTEGER AS $$
DECLARE
new_allowed INTEGER;
BEGIN
        -- Попытка вставить запись
    INSERT INTO user_ratelimiter (user_id, limiter_range, allowed, create_dt, update_dt)
    VALUES (p_user_id, p_limiter_range, p_allowed, p_create_dt, null)
        RETURNING allowed INTO new_allowed;

    RETURN new_allowed;  -- Успешная вставка, возвращаем значение allowed

    EXCEPTION
        WHEN unique_violation OR exclusion_violation THEN
            -- Нарушение исключающего ограничения, выполняем обновление
            UPDATE user_ratelimiter
            SET allowed = allowed - 1, update_dt = p_update_dt
            WHERE user_id = p_user_id AND limiter_range && p_limiter_range
                    RETURNING allowed INTO new_allowed;
            RETURN new_allowed;  -- Успешное обновление, возвращаем новое значение allowed
END;
$$ LANGUAGE plpgsql;


