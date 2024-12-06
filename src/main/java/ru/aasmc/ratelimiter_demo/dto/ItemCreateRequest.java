package ru.aasmc.ratelimiter_demo.dto;

public record ItemCreateRequest(
        String user,
        String itemName
) {
}
