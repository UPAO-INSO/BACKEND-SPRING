package team.upao.dev.customers.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.upao.dev.customers.dto.CustomerRequestRequestDto;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.customers.enums.DocumentType;
import team.upao.dev.customers.mapper.CustomerMapper;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.customers.repository.ICustomerRepository;
import team.upao.dev.customers.service.CustomerService;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.exceptions.ResourceNotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ICustomerRepository clientRepository;
    private final CustomerMapper customerMapper;

    private DocumentType choiceDocumentType(String documentNumber) {
        if (documentNumber.length() == 8) {
            return DocumentType.DNI;
        } else if (documentNumber.length() == 11) {
            return DocumentType.RUC;
        } else {
            throw new IllegalArgumentException("Invalid document number length");
        }
    }

    @Override
    public CustomerResponseDto create(CustomerRequestRequestDto clientRequestDto) {
        CustomerModel customerModel = customerMapper.toModel(clientRequestDto);

        DocumentType documentType = this.choiceDocumentType(customerModel.getDocumentNumber());
        customerModel.setDocumentType(documentType);

        log.info("Creating customer: {}", customerModel);

        return customerMapper.toDto(clientRepository.save(customerModel));
    }

    @Override
    public PaginationResponseDto<CustomerResponseDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<CustomerModel> entities = clientRepository.findAll(pageable);
        final List<CustomerResponseDto> entitiesDto = customerMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                entitiesDto,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    public CustomerResponseDto findById(Long id) {
        CustomerModel client = this.clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
        return customerMapper.toDto(client);
    }

    @Override
    public CustomerModel findModelById(Long id) {
        return this.clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));
    }

    @Override
    public CustomerResponseDto update(Long id, CustomerRequestRequestDto clientRequestDto) {
        CustomerModel client = this.findModelById(id);

        client.setName(clientRequestDto.getName());
        client.setLastname(clientRequestDto.getLastname());
        client.setEmail(clientRequestDto.getEmail());
        client.setPhone(clientRequestDto.getPhone());

        return  customerMapper.toDto(clientRepository.save(client));
    }

    @Override
    public CustomerResponseDto partialUpdate(Long aLong, CustomerResponseDto dto) {
        return null;
    }

    @Override
    public String delete(Long id) {
        return "Client with id " + id + " deleted successfully";
    }

    @Override
    public CustomerResponseDto findByEmail(String email) {
        CustomerModel client = clientRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with email: " + email));

        return customerMapper.toDto(client);
    }

    @Override
    public CustomerResponseDto findByPhone(String phone) {
        CustomerModel client = clientRepository
                .findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with phone: " + phone));

        return customerMapper.toDto(client);
    }

    @Override
    public List<CustomerResponseDto> findByDocument(String document) {
        List<CustomerModel> client = clientRepository
                .findByDocumentNumberContaining(document).stream().toList();

        return customerMapper.toDto(client);
    }
}
