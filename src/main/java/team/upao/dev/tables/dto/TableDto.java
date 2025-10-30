package team.upao.dev.tables.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import team.upao.dev.tables.enums.TableStatus;

@Getter @Setter
@Builder
public class TableDto {

    @NotBlank
    private String number;

    @NotBlank
    private Integer capacity;

    @NotBlank
    private Integer floor;

    @NotNull
    private Boolean isActive;

    @NotNull
    private TableStatus status;
}
