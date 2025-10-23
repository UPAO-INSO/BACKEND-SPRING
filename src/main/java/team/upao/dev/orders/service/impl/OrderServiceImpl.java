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
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TableService tableService;
    private final ProductService productService;
    private final EmployeeService employeeService;

    public double calculateOrderPrice(List<ProductModel> products) {
        // Separar productos por categoría
        List<ProductModel> segundos = products.stream()
                .filter(p -> p.getProductType().getName().equals("SEGUNDOS"))
                .collect(Collectors.toList());

        List<ProductModel> entradas = products.stream()
                .filter(p -> p.getProductType().getName().equals("ENTRADAS"))
                .collect(Collectors.toList());

        List<ProductModel> otros = products.stream()
                .filter(p -> {
                    String name = p.getProductType().getName();
                    return !name.equals("SEGUNDOS") && !name.equals("ENTRADAS");
                })
                .collect(Collectors.toList());

        // Calcular precio total
        return calculateTotal(segundos, entradas, otros);
    }

    private double calculateTotal(List<ProductModel> segundos,
                                  List<ProductModel> entradas,
                                  List<ProductModel> otros) {
        double total = 0.0;

        // Sumar precio de los segundos (incluyen entrada gratis)
        total += segundos.stream()
                .mapToDouble(p -> p.getPrice() == null ? 0.0 : p.getPrice())
                .sum();

        // Calcular entradas que se cobran por separado
        int entradasGratis = segundos.size();
        int entradasACobrar = Math.max(0, entradas.size() - entradasGratis);

        if (entradasACobrar > 0) {
            total += entradas.stream()
                    .limit(entradasACobrar)
                    .mapToDouble(p -> p.getPrice() == null ? 0.0 : p.getPrice().doubleValue())
                    .sum();
        }

        // Sumar otros productos
        total += otros.stream()
                .mapToDouble(p -> p.getPrice() == null ? 0.0 : p.getPrice().doubleValue())
                .sum();

        return total;
    }

    @Override
    @Transactional
    public OrderDto create(OrderDto order) {
        OrderModel orderModel = orderMapper.toModel(order);

        if (order.getTableId() != null) {
            TableModel table = tableService.findById(order.getTableId());
            orderModel.setTable(table);
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

        orderModel.setOrderStatus(OrderStatus.PENDING);
        orderModel.setTotalItems(totalItems);
        orderModel.setPaid(order.getPaid() != null ? order.getPaid() : false);

        productOrderModel.forEach(productOrder -> productOrder.setOrder(orderModel));

        orderEmployeeModel.forEach(orderEmployee -> orderEmployee.setOrder(orderModel));

        TableModel table = tableService.findById(orderModel.getTable().getId());

        this.tableService.changeStatus(table.getId(), TableStatus.OCCUPIED);

        List<ProductModel> products = orderModel.getProductOrders()
                .stream()
                .map(ProductOrderModel::getProduct)
                .toList();

        double totalPrice = this.calculateOrderPrice(products);

        orderModel.setTotalPrice(totalPrice);

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
    public PaginationResponseDto<OrderDto> findAllByTablesAndStatus(PaginationRequestDto requestDto,
                                                                    List<Long> tableIds,
                                                                    List<OrderStatus> ordersStatus) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByTableIdInAndOrderStatusIn(pageable, tableIds, ordersStatus);
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
    public PaginationResponseDto<OrderDto> findAllByTableId(PaginationRequestDto requestDto, Long tableId) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByTableId(pageable, tableId);
        final List<OrderDto> orderDtos = orderMapper.toDto(entities.getContent()).stream()
                .filter(order -> !order.getOrderStatus().equals(OrderStatus.COMPLETED) &&
                                !order.getOrderStatus().equals(OrderStatus.CANCELLED)).toList();
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
        } else if (newStatus.equals(OrderStatus.COMPLETED)) {
            tableService.changeStatus(order.getTable().getId(), TableStatus.AVAILABLE);
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
