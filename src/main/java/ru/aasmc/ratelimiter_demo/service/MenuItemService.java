package ru.aasmc.ratelimiter_demo.service;

import org.springframework.stereotype.Service;
import ru.aasmc.ratelimiter_demo.storage.model.MenuItem;
import ru.aasmc.ratelimiter_demo.storage.repository.MenuItemRepository;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final UserRateLimiterService userRateLimiterService;

    public MenuItemService(MenuItemRepository menuItemRepository, UserRateLimiterService userRateLimiterService) {
        this.menuItemRepository = menuItemRepository;
        this.userRateLimiterService = userRateLimiterService;
    }

    public void createMenuItem(String name) {
        MenuItem item = new MenuItem();
        item.setName(name);
        menuItemRepository.save(item);
    }

    public List<MenuItem> getMenuItems(String userName) {
        userRateLimiterService.permitRequestOrThrow(userName);
        return menuItemRepository.findAll();
    }

}
