package upao.inso.dclassic.tables.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import upao.inso.dclassic.tables.enums.TableStatus;

@Data
public class TableDto {

    @NotBlank
    private String number;

    @NotBlank
    private Integer capacity;

    @NotBlank
    private Integer floor;

    @NotNull
    private Boolean isActive = true;

    @NotNull
    private TableStatus status = TableStatus.AVAILABLE;
}
