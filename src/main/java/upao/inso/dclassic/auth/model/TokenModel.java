package upao.inso.dclassic.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upao.inso.dclassic.auth.enums.TokenType;
import upao.inso.dclassic.users.model.UserModel;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "tokens")
public class TokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false)
    private boolean revoked;

    @Column(nullable = false)
    private boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public UserModel user;
}
