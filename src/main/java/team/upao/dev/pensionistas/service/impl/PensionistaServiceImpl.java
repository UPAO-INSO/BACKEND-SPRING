package team.upao.dev.pensionistas.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.customers.service.CustomerService;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.pensionistas.dto.PensionistaConsumoResponseDto;
import team.upao.dev.pensionistas.dto.PensionistaRequestDto;
import team.upao.dev.pensionistas.dto.PensionistaResponseDto;
import team.upao.dev.pensionistas.mapper.PensionistaMapper;
import team.upao.dev.pensionistas.model.PensionistaConsumoModel;
import team.upao.dev.pensionistas.model.PensionistaModel;
import team.upao.dev.pensionistas.repository.IPensionistaConsumoRepository;
import team.upao.dev.pensionistas.repository.IPensionistaRepository;
import team.upao.dev.pensionistas.service.PensionistaService;
import team.upao.dev.separaciones.model.SeparacionModel;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PensionistaServiceImpl implements PensionistaService {

    private final IPensionistaRepository pensionistaRepository;
    private final IPensionistaConsumoRepository consumoRepository;
    private final CustomerService customerService;
    private final PensionistaMapper mapper;

    @Override
    @Transactional
    public PensionistaResponseDto create(PensionistaRequestDto dto) {
        CustomerModel customer = customerService.findModelById(dto.getCustomerId());
        PensionistaModel model = mapper.toModel(dto);
        model.setStartDate(LocalDate.now());
        model.setCustomer(customer);

        log.info("Creando pensionista: {}", customer.getName());
        return mapper.toDto(pensionistaRepository.save(model));
    }

    @Override
    public PaginationResponseDto<PensionistaResponseDto> findAll(PaginationRequestDto requestDto) {
        Pageable pageable = PaginationUtils.getPageable(requestDto);
        Page<PensionistaModel> page = pensionistaRepository.findAll(pageable);
        List<PensionistaResponseDto> dtos = mapper.toDto(page.getContent());
        return new PaginationResponseDto<>(
                dtos,
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber() + 1,
                page.isEmpty()
        );
    }

    @Override
    public PensionistaResponseDto findById(Long id) {
        return mapper.toDto(findModelById(id));
    }

    @Override
    public PensionistaModel findModelById(Long id) {
        return pensionistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pensionista no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public PensionistaResponseDto update(Long id, PensionistaRequestDto dto) {
        PensionistaModel model = findModelById(id);
        model.setCustomer(customerService.findModelById(dto.getCustomerId()));
        model.setNotes(dto.getNotes());
        return mapper.toDto(pensionistaRepository.save(model));
    }

    @Override
    public PensionistaResponseDto partialUpdate(Long id, PensionistaResponseDto dto) {
        return null;
    }

    @Override
    @Transactional
    public String delete(Long id) {
        PensionistaModel model = findModelById(id);
        model.setActive(false);
        pensionistaRepository.save(model);
        return "Pensionista con id " + id + " desactivado";
    }

    @Override
    public List<PensionistaResponseDto> findAllActive() {
        return mapper.toDto(pensionistaRepository.findAllByActiveTrue());
    }

    @Override
    public List<PensionistaConsumoResponseDto> findConsumos(Long pensionistaId) {
        findModelById(pensionistaId);
        return mapper.toConsumoDto(
                consumoRepository.findAllByPensionistaIdOrderByDateDesc(pensionistaId)
        );
    }

    @Override
    @Transactional
    public PensionistaResponseDto renew(Long id, Integer additionalCredits) {
        PensionistaModel model = findModelById(id);
        double added = additionalCredits * model.getPlanPricePerMeal();

        model.setCreditsRemaining(model.getCreditsRemaining() + additionalCredits);
        model.setPlanCredits(model.getPlanCredits() + additionalCredits);
        model.setPlanTotalPaid(model.getPlanTotalPaid() + added);
        model.setActive(true);

        log.info("Renovando plan de pensionista {}: +{} créditos", model.getCustomer().getName(), additionalCredits);
        return mapper.toDto(pensionistaRepository.save(model));
    }

    @Override
    @Transactional
    public void registrarConsumo(PensionistaModel pensionista, SeparacionModel separacion) {
        if (pensionista.getCreditsRemaining() <= 0) {
            throw new IllegalStateException("El pensionista no tiene créditos disponibles");
        }

        PensionistaConsumoModel consumo = PensionistaConsumoModel.builder()
                .pensionista(pensionista)
                .separacion(separacion)
                .date(LocalDate.now())
                .priceApplied(pensionista.getPlanPricePerMeal())
                .build();

        consumoRepository.save(consumo);

        pensionista.setCreditsRemaining(pensionista.getCreditsRemaining() - 1);
        if (pensionista.getCreditsRemaining() == 0) {
            pensionista.setActive(false);
            log.info("Pensionista {} agotó sus créditos, plan desactivado", pensionista.getCustomer().getName());
        }
        pensionistaRepository.save(pensionista);
    }
}
