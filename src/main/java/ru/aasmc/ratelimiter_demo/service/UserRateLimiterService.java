package ru.aasmc.ratelimiter_demo.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRateLimiterService {

    private static final String RATE_LIMITER_METRIC = "rate_limiter_event";
    private final UserRateLimiterRepository userRateLimiterRepository;
    private final MeterRegistry meterRegistry;
    @Value("${internal.allowed-request-period}")
    private Duration allowedRequestPeriod;

    @Transactional
    public void permitRequestOrThrow(String userId) {
        try {
            permitRequestOrThrowInternal(userId);
        } catch (UncategorizedSQLException ex) {
            // thrown when select for update no wait doesn't allow to proceed
            log.error(ex.getMessage(), ex);
            registerError(userId);
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
                                registerSuccess(userId);
                            } else {
                                log.error("Request not permitted");
                                registerError(userId);
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request.");
                            }
                        },
                        () -> {
                            UserRateLimiter rl = new UserRateLimiter(null, userId, Instant.now(), null);
                            try {
                                log.info("Trying to permit request.");
                                userRateLimiterRepository.save(rl);
                                registerSuccess(userId);
                            } catch (DbActionExecutionException ex) {
                                log.error("Failed to permit request. Exception = {}", ex.getMessage());
                                registerError(userId);
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request. Error " + ex.getMessage());
                            }
                        }
                );
    }

    private void registerSuccess(String user) {
        meterRegistry.counter(RATE_LIMITER_METRIC, List.of(Tag.of("user", user), Tag.of("status", "success"))).increment();
    }

    private void registerError(String user) {
        meterRegistry.counter(RATE_LIMITER_METRIC, List.of(Tag.of("user", user), Tag.of("status", "failure"))).increment();
    }

}

