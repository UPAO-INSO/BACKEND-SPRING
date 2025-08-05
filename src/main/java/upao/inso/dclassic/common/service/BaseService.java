package upao.inso.dclassic.common.service;

import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;

public interface BaseService<T, K, ID> {
    K create(T dto);
    K findById(ID id);
    K update(ID id, T dto);
    K partialUpdate(ID id, K dto);
    String delete(ID id);
    PaginationResponseDto<K> findAll(PaginationRequestDto requestDto);
}
