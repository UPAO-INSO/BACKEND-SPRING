package upao.inso.dclassic.orders.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upao.inso.dclassic.clients.model.ClientModel;
import upao.inso.dclassic.clients.service.ClientService;
import upao.inso.dclassic.common.dto.PaginationRequestDto;
import upao.inso.dclassic.common.dto.PaginationResponseDto;
import upao.inso.dclassic.common.utils.PaginationUtils;
import upao.inso.dclassic.employees.services.EmployeeService;
import upao.inso.dclassic.exceptions.NotFoundException;
import upao.inso.dclassic.orders.dto.ChangeOrderStatusDto;
import upao.inso.dclassic.orders.dto.OrderDto;
import upao.inso.dclassic.orders.enums.OrderStatus;
import upao.inso.dclassic.orders.mapper.OrderMapper;
import upao.inso.dclassic.orders.model.OrderEmployeeModel;
import upao.inso.dclassic.orders.model.OrderModel;
import upao.inso.dclassic.orders.repository.IOrderRepository;
import upao.inso.dclassic.orders.service.OrderService;
import upao.inso.dclassic.products.model.ProductModel;
import upao.inso.dclassic.products.model.ProductOrderModel;
import upao.inso.dclassic.products.service.ProductService;
import upao.inso.dclassic.tables.model.TableModel;
import upao.inso.dclassic.tables.service.TableService;

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
    public PaginationResponseDto<OrderDto> findAll(PaginationRequestDto requestDto) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAll(pageable);
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

        if (newStatus.equals(OrderStatus.PENDING)) {
            return orderMapper.toDto(order);
        } else if (newStatus.equals(OrderStatus.PAID)) {
            order.setOrderStatus(newStatus);
            order.setPaid(true);
            order.setPaidAt(Instant.now());
            return orderMapper.toDto(orderRepository.save(order));
        }

        order.setOrderStatus(newStatus);

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public String delete(Long id) {
        return "Deleting order with id: " + id;
    }
}
