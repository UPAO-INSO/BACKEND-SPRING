package upao.inso.dclassic.clients.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.clients.repository.IClientRepository;
import upao.inso.dclassic.clients.service.ClientService;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final IClientRepository clientRepository;

    @Override
    public ClientModel create(ClientModel clientDto) {
        return this.clientRepository.save(clientDto);
    }

    @Override
    public PaginationResponseDto<ClientModel> findAll(PaginationRequestDto requestDto) {
        return null;
    }

    @Override
    public ClientModel findById(Long id) {
        return null;
    }

    @Override
    public ClientModel update(Long id, ClientModel clientDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public ClientModel findByEmail(String email) {
        return null;
    }

    @Override
    public ClientModel findByPhone(String phone) {
        return null;
    }

    @Override
    public ClientModel findByDni(String dni) {
        return null;
    }

    @Override
    public ClientModel findByRuc(String ruc) {
        return null;
    }
}
