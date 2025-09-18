package team.upao.dev.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Sort;

@Data
@SuperBuilder
@NoArgsConstructor @AllArgsConstructor
public class PaginationRequestDto {

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer limit = 10;

    @Builder.Default
    private String sortField = "id";

    @Builder.Default
    private Sort.Direction direction = Sort.Direction.ASC;
}
