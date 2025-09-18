package team.upao.dev.clients.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.upao.dev.clients.dto.ClientRequestDto;
import team.upao.dev.clients.dto.ClientResponseDto;
import team.upao.dev.clients.mapper.ClientMapper;
import team.upao.dev.clients.model.ClientModel;
import team.upao.dev.clients.repository.IClientRepository;
import team.upao.dev.clients.service.ClientService;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final IClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponseDto create(ClientRequestDto clientRequestDto) {
        ClientModel clientModel = clientMapper.toModel(clientRequestDto);
        return clientMapper.toDto(clientRepository.save(clientModel));
    }

    @Override
    public PaginationResponseDto<ClientResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<ClientModel> entities = clientRepository.findAll(pageable);
        final List<ClientResponseDto> entitiesDto = clientMapper.toDto(entities.getContent());
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
    public ClientResponseDto findById(Long id) {
        ClientModel client = this.clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
        return clientMapper.toDto(client);
    }

    @Override
    public ClientModel findModelById(Long id) {
        return this.clientRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Client not found with id: " + id));
    }

    @Override
    public ClientResponseDto update(Long id, ClientRequestDto clientRequestDto) {
        ClientModel client = this.findModelById(id);

        client.setName(clientRequestDto.getName());
        client.setLastname(clientRequestDto.getLastname());
        client.setEmail(clientRequestDto.getEmail());
        client.setPhone(clientRequestDto.getPhone());

        return  clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public ClientResponseDto partialUpdate(Long aLong, ClientResponseDto dto) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return "Client with id " + id + " deleted successfully";
    }

    @Override
    public ClientResponseDto findByEmail(String email) {
        ClientModel client = clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Client not found with email: " + email));

        return clientMapper.toDto(client);
    }

    @Override
    public ClientResponseDto findByPhone(String phone) {
        ClientModel client = clientRepository
                .findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("Client not found with phone: " + phone));

        return clientMapper.toDto(client);
    }

    @Override
    public ClientResponseDto findByDocument(String document) {
        ClientModel client = clientRepository
                .findByDocumentNumber(document)
                .orElseThrow(() -> new NotFoundException("Client not found with document: " + document));

        return clientMapper.toDto(client);
    }
}
