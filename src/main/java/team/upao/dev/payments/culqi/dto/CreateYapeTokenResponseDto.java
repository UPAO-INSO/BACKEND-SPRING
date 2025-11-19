package team.upao.dev.payments.culqi.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateYapeTokenResponseDto {
    private String tokenId;
    private Long orderId;
    private Long amount;
    private String currency;
    private Instant expiresAt;
}
