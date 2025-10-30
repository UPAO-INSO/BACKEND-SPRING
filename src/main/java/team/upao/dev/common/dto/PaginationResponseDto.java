package team.upao.dev.common.dto;

import lombok.*;

import java.util.Collection;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PaginationResponseDto<T> {
    private Collection<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer page;
    private Boolean empty;
}
