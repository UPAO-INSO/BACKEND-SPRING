package team.upao.dev.orders.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.clients.model.ClientModel;
import team.upao.dev.clients.service.ClientService;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.employees.services.EmployeeService;
import team.upao.dev.exceptions.NotFoundException;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.mapper.OrderMapper;
import team.upao.dev.orders.model.OrderEmployeeModel;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.orders.repository.IOrderRepository;
import team.upao.dev.orders.service.OrderService;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductOrderModel;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TableService tableService;
    private final ClientService clientService;
    private final ProductService productService;
    private final EmployeeService employeeService;

    @Override
    @Transactional
    public OrderDto create(OrderDto order) {
        OrderModel orderModel = orderMapper.toModel(order);

        if (order.getTableId() != null) {
            TableModel table = tableService.findById(order.getTableId());
            orderModel.setTable(table);
        }

        if (order.getClientId() != null) {
            ClientModel client = clientService.findModelById(order.getClientId());
            orderModel.setClient(client);
        }

        List<ProductOrderModel> productOrderModel = order
                .getProductOrders()
                .stream()
                .map(productOrderDto -> {
                    ProductModel product = productService.findModelById(productOrderDto.getProductId());

                    return ProductOrderModel.builder()
                            .quantity(productOrderDto.getQuantity())
                            .unitPrice(product.getPrice())
                            .subtotal(productOrderDto.getQuantity() * product.getPrice())
                            .product(product)
                            .build();
                })
                .toList();

        List<OrderEmployeeModel> orderEmployeeModel = order
                .getOrderEmployees()
                .stream()
                .map(orderEmployeeDto -> OrderEmployeeModel.builder()
                        .employee(employeeService.findModelById(orderEmployeeDto.getEmployeeId()))
                        .minutesSpent(0)
                        .build())
                .toList();

        orderModel.setProductOrders(productOrderModel);
        orderModel.setOrdersEmployee(orderEmployeeModel);

        int totalItems = orderModel
                .getProductOrders()
                .stream()
                .mapToInt(ProductOrderModel::getQuantity)
                .sum();

        double totalPrice = orderModel
                .getProductOrders()
                .stream()
                .mapToDouble(ProductOrderModel::getSubtotal)
                .sum();

        orderModel.setOrderStatus(OrderStatus.PENDING);
        orderModel.setTotalItems(totalItems);
        orderModel.setTotalPrice(totalPrice);
        orderModel.setPaid(order.getPaid() != null ? order.getPaid() : false);

        productOrderModel.forEach(productOrder -> productOrder.setOrder(orderModel));

        orderEmployeeModel.forEach(orderEmployee -> orderEmployee.setOrder(orderModel));

        OrderModel saved = orderRepository.save(orderModel);

        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderDto> findAll(PaginationRequestDto requestDto, OrderStatus status) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities;

        if (status != null) {
            entities = orderRepository.findAllByOrderStatus(pageable, status);
        } else {
            entities = orderRepository.findAll(pageable);
        }

        final List<OrderDto> orderDtos = orderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderDto> findAllByArrayStatus(PaginationRequestDto requestDto,
                                                                List<OrderStatus> ordersStatus) {
        System.out.println(requestDto);
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByOrderStatusIn(pageable, ordersStatus);
        final List<OrderDto> orderDtos = orderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        OrderModel order = orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findByTableIds(List<Long> tableIds) {
        List<OrderModel> order = orderRepository
                .findByTableIdIn(tableIds)
                .orElseThrow(() -> new NotFoundException("No pending orders found for the given table IDs"));

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderModel findModelById(Long id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public OrderDto update(Long id, OrderDto order) {
        OrderModel orderModel = this.findModelById(id);

        orderModel.setOrderStatus(order.getOrderStatus());
        orderModel.setComment(order.getComment());
        orderModel.setPaid(order.getPaid());

        orderRepository.save(orderModel);

        return orderMapper.toDto(orderModel);
    }

    @Override
    @Transactional
    public OrderDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto) {
        OrderModel order = this.findModelById(changeOrderStatusDto.getOrderId());

        OrderStatus newStatus = changeOrderStatusDto.getStatus();

        if (newStatus.equals(OrderStatus.PAID)) {
            order.setOrderStatus(newStatus);
            order.setPaid(true);
            order.setPaidAt(Instant.now());
            return orderMapper.toDto(orderRepository.save(order));
        }

        order.setOrderStatus(newStatus);
        order.setPaidAt(null);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        return "Deleting order with id: " + id;
    }
}
