package team.upao.dev.menu.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
@Builder
public class MenuDiarioItemResponseDto {
    private Long id;
    private Long productId;
    private String productName;
    private String productType;
    private String imageUrl;
    private Double productPrice;
    private LocalDate date;
    private Integer estimatedPortions;
    private Integer usedPortions;
    private Integer remainingPortions;
    private Boolean soldOut;
}
