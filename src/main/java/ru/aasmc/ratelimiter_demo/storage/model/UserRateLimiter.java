package ru.aasmc.ratelimiter_demo.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
