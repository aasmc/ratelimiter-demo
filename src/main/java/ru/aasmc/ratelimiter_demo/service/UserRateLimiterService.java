package ru.aasmc.ratelimiter_demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aasmc.ratelimiter_demo.exception.ServiceException;
import ru.aasmc.ratelimiter_demo.storage.model.TimestampRange;
import ru.aasmc.ratelimiter_demo.storage.repository.UserRateLimiterRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRateLimiterService {

    private static final Duration LIFETIME = Duration.ofSeconds(2);
    private static final int ALLOWED_REQUESTS = 1;
    private final UserRateLimiterRepository userRateLimiterRepository;


    @Transactional
    public void permitRequestOrThrow(String userId) {
        OffsetDateTime from = OffsetDateTime.now();
        OffsetDateTime to = from.plus(LIFETIME);
        int allowed = userRateLimiterRepository.insertAndReturnAllowed(
                userId,
                LocalDateTime.now(),
                null,
                new TimestampRange(from, to),
                ALLOWED_REQUESTS);
        if (allowed <= 0) {
            log.error("Cannot allow request");
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot permit request.");
        }
        log.info("Request can proceed.");
    }
}
