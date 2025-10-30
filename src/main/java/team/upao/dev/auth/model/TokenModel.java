package team.upao.dev.auth.model;

import jakarta.persistence.*;
import lombok.*;
import team.upao.dev.auth.enums.TokenType;
import team.upao.dev.users.model.UserModel;

@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(
        name = "tokens",
        indexes = {
                @Index(name = "idx_tokens_user_id", columnList = "user_id"),
                @Index(name = "idx_tokens_token", columnList = "token", unique = true)
        }
)
public class TokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 300)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType tokenType;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean revoked = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean expired = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public UserModel user;
}
