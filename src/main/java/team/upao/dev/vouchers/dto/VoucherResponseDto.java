package team.upao.dev.vouchers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor @NoArgsConstructor
public class VoucherResponseDto {
    private Long id;
    private String pdfUrl;
}
