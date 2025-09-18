package team.upao.dev.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PaginationResponseDto<T> {
    private Collection<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer page;
    private Boolean empty;
}
