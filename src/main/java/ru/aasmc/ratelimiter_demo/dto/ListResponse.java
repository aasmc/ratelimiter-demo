package ru.aasmc.ratelimiter_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.aasmc.ratelimiter_demo.storage.model.MenuItem;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponse {
    private List<MenuItem> items;
    private String user;
}
