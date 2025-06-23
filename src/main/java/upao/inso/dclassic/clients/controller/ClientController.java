package upao.inso.dclassic.clients.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upao.inso.dclassic.clients.service.ClientService;

@RestController
@RequiredArgsConstructor
@RequestMapping("clients")
public class ClientController {
    private final ClientService clientService;
}
