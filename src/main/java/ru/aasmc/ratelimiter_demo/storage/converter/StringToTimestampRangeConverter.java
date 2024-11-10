package ru.aasmc.ratelimiter_demo.storage.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import ru.aasmc.ratelimiter_demo.storage.model.TimestampRange;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@ReadingConverter
public class StringToTimestampRangeConverter implements Converter<String, TimestampRange> {

    @Override
    public TimestampRange convert(String source) {
        String[] parts = source.replace("[", "").replace("]", "").split(",");
        OffsetDateTime start = OffsetDateTime.parse(parts[0], DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        OffsetDateTime end = OffsetDateTime.parse(parts[1], DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        TimestampRange range = new TimestampRange();
        range.setStart(start);
        range.setEnd(end);
        return range;
    }
}

