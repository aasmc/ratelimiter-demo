package ru.aasmc.ratelimiter_demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.aasmc.ratelimiter_demo.dto.ListResponse;
import ru.aasmc.ratelimiter_demo.service.MenuItemService;
import ru.aasmc.ratelimiter_demo.storage.model.MenuItem;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @PostMapping("/create/{name}")
    @ResponseStatus(CREATED)
    public ResponseEntity<String> create(@PathVariable("name") String name) {
        log.info("Creating menu with name = {}", name);
        service.createMenuItem(name);
        return ResponseEntity.status(CREATED).body("Created menu " + name);
    }

    @GetMapping("/get/{userId}")
    public ListResponse get(@PathVariable("userId") String userId) {
        log.info("Retrieving menus for userId = {}", userId);
        List<MenuItem> items = service.getMenuItems(userId);
        return new ListResponse(items, userId);
    }

}