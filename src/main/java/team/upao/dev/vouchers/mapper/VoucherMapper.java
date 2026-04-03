package team.upao.dev.vouchers.mapper;

import org.springframework.stereotype.Component;
import team.upao.dev.vouchers.dto.VoucherRequestDto;
import team.upao.dev.vouchers.dto.VoucherResponseDto;
import team.upao.dev.vouchers.enums.CurrencyType;
import team.upao.dev.vouchers.enums.VoucherType;
import team.upao.dev.vouchers.model.VoucherModel;

import java.util.List;

@Component
public class VoucherMapper {
    public VoucherResponseDto toDto(VoucherModel voucherModel) {
        return VoucherResponseDto.builder()
                .id(voucherModel.getId())
                .pdfUrl(voucherModel.getPdfUrl())
                .build();
    }

    public List<VoucherResponseDto> toDto(List<VoucherModel> voucherModels) {
        return voucherModels.stream()
                .map(this::toDto)
                .toList();
    }

    public VoucherModel toModel(VoucherRequestDto dto) {
        if (dto == null) return null;

        VoucherType type = VoucherType.RECEIPT;
        if (dto.getSeries() != null && dto.getSeries().toUpperCase().startsWith("F")) {
            type = VoucherType.INVOICE;
        }

        // Mapear moneda: 1=PEN (default), 2=USD
        CurrencyType currency = CurrencyType.PEN;
        if (dto.getMoneda() != null && dto.getMoneda() == 2) {
            currency = CurrencyType.PEN;
        }

        return VoucherModel.builder()
                .series(dto.getSeries() != null ? dto.getSeries() : "")
                .number(String.valueOf(dto.getNumero()))
                .voucherType(type)
                .currency(currency)
                .igvPercentage(dto.getPorcentajeDeIgv() != null ? dto.getPorcentajeDeIgv() : 18.0)
                .totalGravada(dto.getTotalGravada() != null ? dto.getTotalGravada() : 0.0)
                .totalIgv(dto.getTotalIgv() != null ? dto.getTotalIgv() : 0.0)
                .total(dto.getTotal() != null ? dto.getTotal() : 0.0)
                .observations(dto.getObservaciones() != null ? dto.getObservaciones() : "")
                .pdfUrl(dto.getPdfUrl() != null ? dto.getPdfUrl() : "")
                .xmlUrl(dto.getXmlUrl() != null ? dto.getXmlUrl() : "")
                .qrCodeString(dto.getQrCodeString() != null ? dto.getQrCodeString() : "")
                .barCode(dto.getBarCode() != null ? dto.getBarCode() : "")
                .build();
    }
}
