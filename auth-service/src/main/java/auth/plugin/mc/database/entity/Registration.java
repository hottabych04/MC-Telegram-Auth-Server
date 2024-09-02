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
@Table(name = "registration")
public class Registration {

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Id
    @Column(name = "registration_hash", nullable = false)
    private String registrationHash;

}
