package upao.inso.dclassic.clients.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.clients.dto.ClientDto;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.clients.service.ClientService;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ClientModel create(@RequestBody @Valid ClientDto clientDto) {
        return clientService.create(clientDto);
    }

    @GetMapping
    public PaginationResponseDto<ClientModel> findAll(PaginationRequestDto requestDto) {
        return clientService.findAll(requestDto);
    }

    @GetMapping("/{id}")
    public ClientModel findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @GetMapping("/email/{email}")
    public ClientModel findByEmail(@PathVariable String email) {
        return clientService.findByEmail(email);
    }

    @GetMapping("/phone/{phone}")
    public ClientModel findByPhone(@PathVariable String phone) {
        return clientService.findByPhone(phone);
    }

    @GetMapping("/document/{document}")
    public ClientModel findByDocument(@PathVariable String document) {
        return clientService.findByDocument(document);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ClientModel update(@PathVariable Long id, @RequestBody @Valid ClientDto clientDto) {
        return clientService.update(id, clientDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        return clientService.delete(id);
    }
}
