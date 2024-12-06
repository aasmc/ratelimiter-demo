package ru.aasmc.ratelimiter_demo.dto;

import java.util.List;

public record UserItemsResponse(
        List<String> items,
        String user
) {
}
