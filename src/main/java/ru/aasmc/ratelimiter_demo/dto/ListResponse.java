package ru.aasmc.ratelimiter_demo.dto;

import ru.aasmc.ratelimiter_demo.storage.model.MenuItem;

import java.util.List;

public class ListResponse {
    private List<MenuItem> items;
    private String user;

    public ListResponse(List<MenuItem> items, String user) {
        this.items = items;
        this.user = user;
    }

    public ListResponse() {
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
