package upao.inso.dclassic.clients.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import upao.inso.dclassic.clients.dto.ClientRequestDto;
import upao.inso.dclassic.clients.dto.ClientResponseDto;
import upao.inso.dclassic.clients.service.ClientService;
import upao.inso.dclassic.common.controller.BaseController;
import upao.inso.dclassic.common.service.BaseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController extends BaseController<ClientRequestDto, ClientResponseDto, Long> {
    private final ClientService clientService;

    @Override
    protected BaseService<ClientRequestDto, ClientResponseDto, Long> getService() {
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
