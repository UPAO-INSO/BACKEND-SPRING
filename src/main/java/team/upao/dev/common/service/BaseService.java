package team.upao.dev.common.service;

import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;

public interface BaseService<T, K, ID> {
    K create(T dto);
    K findById(ID id);
    K update(ID id, T dto);
    K partialUpdate(ID id, K dto);
    String delete(ID id);
    PaginationResponseDto<K> findAll(PaginationRequestDto requestDto);
}
