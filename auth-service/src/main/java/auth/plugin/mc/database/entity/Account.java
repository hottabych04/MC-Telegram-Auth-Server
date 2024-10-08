package auth.plugin.mc.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "account")
public class Account {

    @Id
    private String uuid;

    @Column(nullable = false)
    private String username;

    @Column(name = "telegram_id")
    private String telegramId;
}
