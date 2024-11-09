package ru.aasmc.ratelimiter_demo.storage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.aasmc.ratelimiter_demo.storage.model.MenuItem;

import java.util.List;

@Repository
public interface MenuItemRepository extends CrudRepository<MenuItem, Long> {

    List<MenuItem> findAll();
}
