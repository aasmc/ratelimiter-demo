package ru.aasmc.ratelimiter_demo.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.aasmc.ratelimiter_demo.dto.ItemCreateRequest;
import ru.aasmc.ratelimiter_demo.dto.UserItemsResponse;
import ru.aasmc.ratelimiter_demo.storage.model.Item;
import ru.aasmc.ratelimiter_demo.storage.repository.ItemsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemsService {

    private static final String ITEM_METRIC = "item_event";

    private final ItemsRepository itemsRepository;
    private final UserRateLimiterService userRateLimiterService;
    private final MeterRegistry meterRegistry;

    public void createItem(ItemCreateRequest request) {
        Item item = new Item(null, request.itemName(), request.user());
        itemsRepository.save(item);
        registerCreate(request.user());
    }

    public UserItemsResponse getItemsOfUser(String userName) {
        userRateLimiterService.permitRequestOrThrow(userName);
        List<String> itemNames = itemsRepository.findAllByUser(userName)
                .stream()
                .map(Item::getName)
                .toList();
        registerGet(userName);
        return new UserItemsResponse(itemNames, userName);
    }

    private void registerCreate(String user) {
        meterRegistry.counter(ITEM_METRIC, List.of(Tag.of("user", user), Tag.of("event", "create"))).increment();
    }

    private void registerGet(String user) {
        meterRegistry.counter(ITEM_METRIC, List.of(Tag.of("user", user), Tag.of("event", "get"))).increment();
    }

}
