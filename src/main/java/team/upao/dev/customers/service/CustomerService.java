package team.upao.dev.customers.service;

import team.upao.dev.customers.dto.CustomerRequestRequestDto;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.common.service.BaseService;

import java.util.List;

public interface CustomerService extends BaseService<CustomerRequestRequestDto, CustomerResponseDto, Long> {
    CustomerResponseDto findByEmail(String email);
    CustomerResponseDto findByPhone(String phone);
    List<CustomerResponseDto> findByDocument(String document);
    CustomerModel findModelById(Long id);
}
