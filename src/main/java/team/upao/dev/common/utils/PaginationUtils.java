package team.upao.dev.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.upao.dev.common.dto.PaginationRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PaginationUtils {
    public static Pageable getPageable(PaginationRequestDto request) {
        int page = request.getPage() != null ? Math.max(request.getPage() - 1, 0) : 0;
        return PageRequest.of(page, request.getLimit(), request.getDirection(), request.getSortField());
    }
}
