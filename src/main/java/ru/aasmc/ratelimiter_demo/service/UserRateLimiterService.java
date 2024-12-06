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
    private static final String USER_TAG = "user";
    private static final String STATUS_TAG = "status";
    private static final String STATUS_SUCCESS = "success";
    private static final String STATUS_ERROR = "failure";

    private final UserRateLimiterRepository userRateLimiterRepository;
    private final MeterRegistry meterRegistry;
    @Value("${internal.allowed-request-period}")
    private Duration allowedRequestPeriod;

    @Transactional
    public void permitRequestOrThrow(String userName) {
        try {
            permitRequestOrThrowInternal(userName);
        } catch (UncategorizedSQLException ex) {
            // thrown when select for update no wait doesn't allow to proceed
            log.error(ex.getMessage());
            registerMetric(userName, STATUS_ERROR);
            throw new ServiceException(HttpStatus.BAD_REQUEST,  "Cannot allow request. Error " + ex.getMessage());
        }
    }

    private void permitRequestOrThrowInternal(String userName) {
        userRateLimiterRepository.findByUserIdForUpdateNoWait(userName)
                .ifPresentOrElse(
                        rl -> {
                            Instant now = Instant.now();
                            if (allowedToProceed(rl, now)) {
                                rl.setCreated(now);
                                rl.setUpdated(now);
                                userRateLimiterRepository.save(rl);
                                log.info("Request permitted");
                                registerMetric(userName, STATUS_SUCCESS);
                            } else {
                                log.error("Request not permitted");
                                registerMetric(userName, STATUS_ERROR);
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request.");
                            }
                        },
                        () -> {
                            UserRateLimiter rl = new UserRateLimiter(null, userName, Instant.now(), null);
                            try {
                                log.info("Trying to permit request.");
                                userRateLimiterRepository.save(rl);
                                registerMetric(userName, STATUS_SUCCESS);
                            } catch (DbActionExecutionException ex) {
                                log.error("Failed to permit request. Exception = {}", ex.getMessage());
                                registerMetric(userName, STATUS_ERROR);
                                throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot allow request. Error " + ex.getMessage());
                            }
                        }
                );
    }

    private boolean allowedToProceed(UserRateLimiter rl, Instant now) {
        //       create_dt   (now - allowedRequestPeriod)    now
        // time: ___|_________________|_____________________|_____
        return now.minus(allowedRequestPeriod).isAfter(rl.getCreated());
    }

    private void registerMetric(String user, String status) {
        meterRegistry.counter(RATE_LIMITER_METRIC, List.of(Tag.of(USER_TAG, user), Tag.of(STATUS_TAG, status))).increment();
    }

}

