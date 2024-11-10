package ru.aasmc.ratelimiter_demo.storage.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import ru.aasmc.ratelimiter_demo.storage.model.TimestampRange;

import java.time.format.DateTimeFormatter;

@WritingConverter
public class TimestampRangeToStringConverter implements Converter<TimestampRange, String> {

    @Override
    public String convert(TimestampRange source) {
        return "[" + source.getStart().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "," +
                source.getEnd().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "]";
    }
}
