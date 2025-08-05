package upao.inso.dclassic.clients.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import upao.inso.dclassic.clients.dto.ClientDto;
import upao.inso.dclassic.clients.model.ClientModel;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientMapper {
    public ClientModel toEntity(ClientDto clientDto) {
        ClientModel entity = new ClientModel();
        entity.setName(clientDto.getName());
        entity.setLastname(clientDto.getLastname());
        entity.setPhone(clientDto.getPhone());
        entity.setEmail(clientDto.getEmail());
        entity.setDocumentNumber(clientDto.getDocumentNumber());
        return entity;
    }
}
