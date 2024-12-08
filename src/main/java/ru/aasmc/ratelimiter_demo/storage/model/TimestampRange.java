package ru.aasmc.ratelimiter_demo.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimestampRange {

    private OffsetDateTime start;
    private OffsetDateTime end;

    @Override
    public String toString() {
        return "[" + start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) +
                "," + end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + ")";
    }

    public static TimestampRange fromString(String source) {
        String[] parts = source.replace("[", "").replace(")", "").split(",");
        OffsetDateTime start = OffsetDateTime.parse(parts[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime end = OffsetDateTime.parse(parts[1], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return new TimestampRange(start, end);
    }
}
