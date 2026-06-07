package team.upao.dev.vouchers.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import team.upao.dev.vouchers.enums.VoucherType;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class VoucherResponseDto {
    private Long id;

    private String  series;
    private String  number;
    private VoucherType voucherType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant issuedAt;

    private Double  totalGravada;
    private Double  totalIgv;
    private Double  total;
    private Double  igvPercentage;
    private String  currency;
    private String  observations;

    private String  pdfUrl;
    private String  xmlUrl;
    private String  qrCodeString;

    /** ID del pago asociado (null si aún no está asociado) */
    private Long    paymentId;
}
