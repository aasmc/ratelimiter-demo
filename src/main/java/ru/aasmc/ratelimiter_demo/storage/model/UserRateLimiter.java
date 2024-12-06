package ru.aasmc.ratelimiter_demo.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user_ratelimiter")
public class UserRateLimiter {
    @Id
    private Long id;
    @Column("user_name")
    private String userName;
    @Column("create_dt")
    private Instant created;
    @Column("update_dt")
    private Instant updated;

}
