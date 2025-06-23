package upao.inso.dclassic.clients.service;

import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;

public interface ClientService {
    ClientModel create(ClientModel clientDto);
    PaginationResponseDto<ClientModel> findAll(PaginationRequestDto requestDto);
    ClientModel findById(Long id);
    ClientModel update(Long id, ClientModel clientDto);
    void delete(Long id);
    ClientModel findByEmail(String email);
    ClientModel findByPhone(String phone);
    ClientModel findByDni(String dni);
    ClientModel findByRuc(String ruc);

}
