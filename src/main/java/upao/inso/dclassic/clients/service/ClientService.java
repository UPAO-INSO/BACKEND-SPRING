package upao.inso.dclassic.clients.service;

import upao.inso.dclassic.clients.dto.ClientDto;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;

public interface ClientService {
    ClientModel create(ClientDto clientDto);
    PaginationResponseDto<ClientModel> findAll(PaginationRequestDto requestDto);
    ClientModel findById(Long id);
    ClientModel update(Long id, ClientDto clientDto);
    String delete(Long id);
    ClientModel findByEmail(String email);
    ClientModel findByPhone(String phone);
    ClientModel findByDocument(String document);

}
