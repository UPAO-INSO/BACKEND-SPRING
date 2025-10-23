package team.upao.dev.clients.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import team.upao.dev.clients.dto.ClientRequestRequestDto;
import team.upao.dev.clients.dto.ClientResponseDto;
import team.upao.dev.clients.service.ClientService;
import team.upao.dev.common.controller.BaseController;
import team.upao.dev.common.service.BaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController extends BaseController<ClientRequestRequestDto, ClientResponseDto, Long> {
    private final ClientService clientService;

    @Override
    protected BaseService<ClientRequestRequestDto, ClientResponseDto, Long> getService() {
        return clientService;
    }

    @GetMapping("/email/{email}")
    public ClientResponseDto findByEmail(@PathVariable String email) {
        return clientService.findByEmail(email);
    }

    @GetMapping("/phone/{phone}")
    public ClientResponseDto findByPhone(@PathVariable String phone) {
        return clientService.findByPhone(phone);
    }

    @GetMapping("/document/{document}")
    public ClientResponseDto findByDocument(@PathVariable String document) {
        return clientService.findByDocument(document);
    }
}
