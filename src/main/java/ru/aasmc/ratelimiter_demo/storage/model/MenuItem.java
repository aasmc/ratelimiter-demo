package ru.aasmc.ratelimiter_demo.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("menu_items")
public class MenuItem {
    @Id
    private Long id;
    private String name;
}
