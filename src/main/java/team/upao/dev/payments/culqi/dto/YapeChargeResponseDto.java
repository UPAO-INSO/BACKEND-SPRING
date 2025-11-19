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
public class YapeChargeResponseDto {
    private String chargeId;
    private Long orderId;
    private Integer amount;
    private String currency;
    private String status;
    private String referenceCode;
    private String authorizationCode;
    private String userMessage;
    private Instant operationDate;
}
