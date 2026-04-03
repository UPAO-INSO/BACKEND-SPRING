package team.upao.dev.customers.mapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import team.upao.dev.customers.dto.CustomerRequestRequestDto;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.customers.model.CustomerModel;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerMapper {
    public CustomerModel toModel(CustomerRequestRequestDto clientRequestDto) {
        CustomerModel entity = new CustomerModel();
        entity.setName(clientRequestDto.getName());
        entity.setLastname(clientRequestDto.getLastname());
        entity.setPhone(clientRequestDto.getPhone());
        entity.setEmail(clientRequestDto.getEmail());
        entity.setDocumentNumber(clientRequestDto.getDocumentNumber());
        entity.setDepartament(clientRequestDto.getDepartment());
        entity.setProvince(clientRequestDto.getProvince());
        entity.setDistrict(clientRequestDto.getDistrict());
        entity.setCompleteAddress(clientRequestDto.getCompleteAddress());
        return entity;
    }

    public List<CustomerModel> toModel(List<CustomerRequestRequestDto> clientRequestDtos) {
        return clientRequestDtos.stream()
                .map(this::toModel)
                .toList();
    }

    public CustomerResponseDto toDto(CustomerModel customerModel) {
        return CustomerResponseDto.builder()
                .id(customerModel.getId())
                .name(customerModel.getName())
                .lastname(customerModel.getLastname())
                .email(customerModel.getEmail())
                .phone(customerModel.getPhone())
                .documentType(customerModel.getDocumentType())
                .documentNumber(customerModel.getDocumentNumber())
                .departament(customerModel.getDepartament())
                .province(customerModel.getProvince())
                .district(customerModel.getDistrict())
                .completeAddress(customerModel.getCompleteAddress())
                .build();
    }

    public List<CustomerResponseDto> toDto(List<CustomerModel> customerModels) {
        return customerModels.stream()
                .map(this::toDto)
                .toList();
    }
}
