package upao.inso.dclassic.clients.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import upao.inso.dclassic.clients.dto.ClientDto;
import upao.inso.dclassic.clients.mapper.ClientMapper;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.clients.repository.IClientRepository;
import upao.inso.dclassic.clients.service.ClientService;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientModel create(ClientDto clientDto) {
        ClientModel clientModel = clientMapper.toEntity(clientDto);
        return this.clientRepository.save(clientModel);
    }

    @Override
    public PaginationResponseDto<ClientModel> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ClientModel> entities = clientRepository.findAll(pageable);
        final List<ClientModel> entitiesDto = entities.stream().toList();
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public ClientModel findById(Long id) {
        return this.clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
    }

    @Override
    public ClientModel update(Long id, ClientDto clientDto) {
        ClientModel client = this.findById(id);

        client.setName(clientDto.getName());
        client.setLastname(clientDto.getLastname());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());

        return clientRepository.save(client);
    }

    @Override
    public String delete(Long id) {
        return "Client with id " + id + " deleted successfully";
    }

    @Override
    public ClientModel findByEmail(String email) {
        return clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));
    }

    @Override
    public ClientModel findByPhone(String phone) {
        return clientRepository
                .findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("Client not found with phone: " + phone));
    }

    @Override
    public ClientModel findByDocument(String document) {
        return clientRepository
                .findByDocumentNumber(document)
                .orElseThrow(() -> new NotFoundException("Client not found with document: " + document));
    }
}
