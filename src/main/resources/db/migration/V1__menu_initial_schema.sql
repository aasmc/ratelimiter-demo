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
existing_allowed INTEGER;
BEGIN
        -- Проверяем наличие пересечения диапазонов
    SELECT allowed INTO existing_allowed
    FROM user_ratelimiter
    WHERE user_id = p_user_id AND limiter_range && p_limiter_range
        FOR UPDATE;

    IF FOUND THEN
            -- Если нашли запись, обновляем её
        UPDATE user_ratelimiter
        SET allowed = allowed - 1, update_dt = p_update_dt
        WHERE user_id = p_user_id AND limiter_range && p_limiter_range;
        RETURN existing_allowed - 1;
    ELSE
            -- Если не нашли, вставляем новую запись
        INSERT INTO user_ratelimiter (user_id, limiter_range, allowed, create_dt, update_dt)
        VALUES (p_user_id, p_limiter_range, p_allowed, p_create_dt, null);
        RETURN p_allowed;
    END IF;
END;
$$ LANGUAGE plpgsql;
