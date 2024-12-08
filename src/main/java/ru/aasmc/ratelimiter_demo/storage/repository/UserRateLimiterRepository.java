package ru.aasmc.ratelimiter_demo.storage.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.ratelimiter_demo.storage.model.TimestampRange;
import ru.aasmc.ratelimiter_demo.storage.model.UserRateLimiter;

import java.time.Instant;
import java.time.LocalDateTime;


@Repository
public interface UserRateLimiterRepository extends CrudRepository<UserRateLimiter, Long> {


    @Modifying
    @Query("insert into user_ratelimiter(user_name, limiter_range, create_dt) " +
            "values(:userName, tstzrange(:range), :created)")
    int insert(String userName, Instant created, String range);

    @Modifying
    @Query("delete from user_ratelimiter where create_dt < :before")
    void deleteBefore(Instant before);

}
