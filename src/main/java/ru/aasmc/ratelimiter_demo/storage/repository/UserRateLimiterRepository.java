package ru.aasmc.ratelimiter_demo.storage.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.ratelimiter_demo.storage.model.TimestampRange;
import ru.aasmc.ratelimiter_demo.storage.model.UserRateLimiter;

import java.time.LocalDateTime;


@Repository
public interface UserRateLimiterRepository extends CrudRepository<UserRateLimiter, Long> {

    @Query("""
        select upsert_user_ratelimiter(:userId, tstzrange(:range), :allowed, :created, :updated);
    """)
    int insertAndReturnAllowed(String userId,
                               LocalDateTime created,
                               LocalDateTime updated,
                               TimestampRange range,
                               int allowed);

}
