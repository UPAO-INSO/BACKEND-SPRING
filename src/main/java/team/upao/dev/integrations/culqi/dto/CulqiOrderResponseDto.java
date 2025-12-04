package team.upao.dev.integrations.culqi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CulqiOrderResponseDto {
    public String object;
    public String id;
    public int amount;
    @JsonProperty("payment_code")
    public String paymentCode;
    @JsonProperty("currency_code")
    public String currencyCode;
    public String description;
    @JsonProperty("order_number")
    public String orderNumber;
    public String state;
    @JsonProperty("total_fee")
    public Object totalFee;
    @JsonProperty("net_amount")
    public Object netAmount;
    @JsonProperty("fee_details")
    public Object feeDetails;
    @JsonProperty("creation_date")
    public Integer creationDate;
    @JsonProperty("expiration_date")
    public Integer expirationDate;
    @JsonProperty("updated_at")
    public Integer updatedAt;
    @JsonProperty("paid_at")
    public Integer paidAt;
    @JsonProperty("available_on")
    public Instant availableOn;
    public String qr;
    public String cuotealo;
    @JsonProperty("url_pe")
    public String urlPe;
}
