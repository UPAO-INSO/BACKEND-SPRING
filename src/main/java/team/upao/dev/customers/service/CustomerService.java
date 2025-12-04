package team.upao.dev.customers.service;

import team.upao.dev.customers.dto.CustomerRequestRequestDto;
import team.upao.dev.customers.dto.CustomerResponseDto;
import team.upao.dev.customers.model.CustomerModel;
import team.upao.dev.common.service.BaseService;
import team.upao.dev.orders.model.OrderModel;

import java.util.List;
import java.util.UUID;

public interface CustomerService extends BaseService<CustomerRequestRequestDto, CustomerResponseDto, Long> {
    CustomerResponseDto findByEmail(String email);
    CustomerResponseDto findByPhone(String phone);
    List<CustomerResponseDto> findByDocument(String document);
    CustomerModel findModelById(Long id);
    CustomerModel findModelByCustomerIdInPayment(Long id);
}
