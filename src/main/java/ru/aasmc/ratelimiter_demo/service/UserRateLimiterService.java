package ru.aasmc.ratelimiter_demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aasmc.ratelimiter_demo.exception.ServiceException;
import ru.aasmc.ratelimiter_demo.storage.model.UserRateLimiter;
import ru.aasmc.ratelimiter_demo.storage.repository.UserRateLimiterRepository;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserRateLimiterService {

    private static final Logger log = LoggerFactory.getLogger(UserRateLimiterService.class);
    private static final Duration LIFETIME = Duration.ofSeconds(2);
    private final UserRateLimiterRepository userRateLimiterRepository;

    public UserRateLimiterService(UserRateLimiterRepository userRateLimiterRepository) {
        this.userRateLimiterRepository = userRateLimiterRepository;
    }

    @Transactional
    public void permitRequestOrThrow(String userId) {
        try {
            permitRequestOrThrowInternal(userId);
        } catch (UncategorizedSQLException ex) {
            log.error(ex.getMessage(), ex);
            throw new ServiceException(HttpStatus.BAD_REQUEST,  "Cannot allow request. Error " + ex.getMessage());
        }
    }

    private void permitRequestOrThrowInternal(String userId) {
        userRateLimiterRepository.findByUserIdForUpdateNoWait(userId)
                .ifPresentOrElse(
                        rl -> {
                            LocalDateTime now = LocalDateTime.now();
                            LocalDateTime created = rl.getCreated();
                            if (now.minus(LIFETIME).isAfter(created)) {
                                rl.setCreated(now);
                                rl.setUpdated(now);
                                userRateLimiterRepository.save(rl);
                                log.info("Request permitted");
                            } else {
                                log.error("Request not permitted");
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request.");
                            }
                        },
                        () -> {
                            UserRateLimiter rl = new UserRateLimiter();
                            rl.setUserId(userId);
                            rl.setCreated(LocalDateTime.now());
                            try {
                                log.info("Trying to permit request.");
                                userRateLimiterRepository.save(rl);
                            } catch (DbActionExecutionException ex) {
                                log.error("Failed to permit request. Exception = {}", ex.getMessage());
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request. Error " + ex.getMessage());
                            }
                        }
                );
    }
}
