package team.upao.dev.clients.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.upao.dev.clients.dto.ClientRequestDto;
import team.upao.dev.clients.dto.ClientResponseDto;
import team.upao.dev.clients.model.ClientModel;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientMapper {
    public ClientModel toModel(ClientRequestDto clientRequestDto) {
        ClientModel entity = new ClientModel();
        entity.setName(clientRequestDto.getName());
        entity.setLastname(clientRequestDto.getLastname());
        entity.setPhone(clientRequestDto.getPhone());
        entity.setEmail(clientRequestDto.getEmail());
        entity.setDocumentNumber(clientRequestDto.getDocumentNumber());
        return entity;
    }

    public List<ClientModel> toModel(List<ClientRequestDto> clientRequestDtos) {
        return clientRequestDtos.stream()
                .map(this::toModel)
                .toList();
    }

    public ClientResponseDto toDto(ClientModel clientModel) {
        return ClientResponseDto.builder()
                .id(clientModel.getId())
                .name(clientModel.getName())
                .lastname(clientModel.getLastname())
                .email(clientModel.getEmail())
                .phone(clientModel.getPhone())
                .documentNumber(clientModel.getDocumentNumber())
                .createdAt(clientModel.getCreatedAt())
                .updatedAt(clientModel.getUpdatedAt())
                .build();
    }

    public List<ClientResponseDto> toDto(List<ClientModel> clientModels) {
        return clientModels.stream()
                .map(this::toDto)
                .toList();
    }
}
