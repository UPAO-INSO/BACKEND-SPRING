package team.upao.dev.integrations.nubefact.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.upao.dev.integrations.nubefact.dto.NubefactInvoiceRequestDto;
import team.upao.dev.integrations.nubefact.dto.NubefactReceiptRequestDto;
import team.upao.dev.integrations.nubefact.service.NubefactService;

@RestController
@RequestMapping("nubefact")
@RequiredArgsConstructor
public class NubefactController {
    private final NubefactService nubefactService;

    @PostMapping("/invoice")
    public ResponseEntity<String> invoice(@RequestBody NubefactInvoiceRequestDto requestBody) {
        return nubefactService.sendInvoice(requestBody);
    }

    @PostMapping("/receipt")
    public ResponseEntity<String> tickets(@RequestBody NubefactReceiptRequestDto requestBody) {
        return nubefactService.sendReceipt(requestBody);
    }
}
