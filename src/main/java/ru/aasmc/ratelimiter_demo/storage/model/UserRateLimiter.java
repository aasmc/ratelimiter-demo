package ru.aasmc.ratelimiter_demo.storage.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("user_ratelimiter")
public class UserRateLimiter {
    @Id
    private Long id;
    @Column("user_id")
    private String userId;
    @Column("create_dt")
    private LocalDateTime created;
    @Column("update_dt")
    private LocalDateTime updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public UserRateLimiter() {
    }

    public UserRateLimiter(Long id, String userId, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.userId = userId;
        this.created = created;
        this.updated = updated;
    }
}
