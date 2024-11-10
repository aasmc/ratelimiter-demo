package ru.aasmc.ratelimiter_demo.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampRange {

    private OffsetDateTime start;
    private OffsetDateTime end;

}
