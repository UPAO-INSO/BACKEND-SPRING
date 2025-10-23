package team.upao.dev.clients.service;

import team.upao.dev.clients.dto.ClientRequestRequestDto;
import team.upao.dev.clients.dto.ClientResponseDto;
import team.upao.dev.clients.model.ClientModel;
import team.upao.dev.common.service.BaseService;

public interface ClientService extends BaseService<ClientRequestRequestDto, ClientResponseDto, Long> {
    ClientResponseDto findByEmail(String email);
    ClientResponseDto findByPhone(String phone);
    ClientResponseDto findByDocument(String document);
    ClientModel findModelById(Long id);
}
