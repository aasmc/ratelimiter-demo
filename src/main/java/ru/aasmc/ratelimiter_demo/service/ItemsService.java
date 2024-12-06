package ru.aasmc.ratelimiter_demo.service;

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

    private final ItemsRepository itemsRepository;
    private final UserRateLimiterService userRateLimiterService;

    public void createItem(ItemCreateRequest request) {
        Item item = new Item(null, request.itemName(), request.user());
        itemsRepository.save(item);
    }

    public UserItemsResponse getItemsOfUser(String userName) {
        userRateLimiterService.permitRequestOrThrow(userName);
        List<String> itemNames = itemsRepository.findAllByUser(userName)
                .stream()
                .map(Item::getName)
                .toList();
        return new UserItemsResponse(itemNames, userName);
    }

}
