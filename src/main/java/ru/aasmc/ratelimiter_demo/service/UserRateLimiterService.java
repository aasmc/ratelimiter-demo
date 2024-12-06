package ru.aasmc.ratelimiter_demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aasmc.ratelimiter_demo.exception.ServiceException;
import ru.aasmc.ratelimiter_demo.storage.model.UserRateLimiter;
import ru.aasmc.ratelimiter_demo.storage.repository.UserRateLimiterRepository;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRateLimiterService {

    private final UserRateLimiterRepository userRateLimiterRepository;
    @Value("${internal.allowed-request-period}")
    private Duration allowedRequestPeriod;

    @Transactional
    public void permitRequestOrThrow(String userId) {
        try {
            permitRequestOrThrowInternal(userId);
        } catch (UncategorizedSQLException ex) {
            // thrown when select for update no wait doesn't allow to proceed
            log.error(ex.getMessage(), ex);
            throw new ServiceException(HttpStatus.BAD_REQUEST,  "Cannot allow request. Error " + ex.getMessage());
        }
    }

    private void permitRequestOrThrowInternal(String userId) {
        userRateLimiterRepository.findByUserIdForUpdateNoWait(userId)
                .ifPresentOrElse(
                        rl -> {
                            Instant now = Instant.now();
                            Instant created = rl.getCreated();
                            if (now.minus(allowedRequestPeriod).isAfter(created)) {
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
                            UserRateLimiter rl = new UserRateLimiter(null, userId, Instant.now(), null);
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
