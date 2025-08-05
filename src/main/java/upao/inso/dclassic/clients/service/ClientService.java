package upao.inso.dclassic.clients.service;

import upao.inso.dclassic.clients.dto.ClientRequestDto;
import upao.inso.dclassic.clients.dto.ClientResponseDto;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.service.BaseService;

public interface ClientService extends BaseService<ClientRequestDto, ClientResponseDto, Long> {
    ClientResponseDto findByEmail(String email);
    ClientResponseDto findByPhone(String phone);
    ClientResponseDto findByDocument(String document);
    ClientModel findModelById(Long id);
}
