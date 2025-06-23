package upao.inso.dclassic.nubefact;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import upao.inso.dclassic.nubefact.dto.NubefactInvoiceRequestDto;
import upao.inso.dclassic.nubefact.service.NubefactService;

@RestController
@RequestMapping("nubefact")
@RequiredArgsConstructor
public class NubefactController {
    private  final NubefactService nubefactService;

    @PostMapping("/invoice")
    public String invoice(@RequestBody NubefactInvoiceRequestDto requestBody) {
        return nubefactService.sendInvoice(requestBody).getBody();
    }
}
