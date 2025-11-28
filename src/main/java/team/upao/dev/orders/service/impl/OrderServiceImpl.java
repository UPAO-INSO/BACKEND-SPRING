package team.upao.dev.orders.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.employees.services.EmployeeService;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.orders.dto.ChangeOrderStatusDto;
import team.upao.dev.orders.dto.OrderRequestDto;
import team.upao.dev.orders.dto.OrderResponseDto;
import team.upao.dev.orders.dto.ServeProductOrderRequestDto;
import team.upao.dev.orders.enums.OrderStatus;
import team.upao.dev.orders.mapper.OrderMapper;
import team.upao.dev.orders.model.OrderEmployeeModel;
import team.upao.dev.orders.model.OrderModel;
import team.upao.dev.orders.repository.IOrderRepository;
import team.upao.dev.orders.service.OrderService;
import team.upao.dev.products.dto.ProductOrderRequestDto;
import team.upao.dev.products.enums.ProductTypeEnum;
import team.upao.dev.products.mapper.ProductOrderMapper;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductOrderModel;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final TableService tableService;
    private final ProductService productService;
    private final EmployeeService employeeService;
    private final ProductOrderMapper productOrderMapper;

    private BigDecimal resolveUnitPrice(ProductOrderModel productOrderModel) {
        if (productOrderModel == null) return BigDecimal.ZERO;

        if (productOrderModel.getUnitPrice() != null) {
            return BigDecimal.valueOf(productOrderModel.getUnitPrice());
        }

        if (productOrderModel.getProduct() != null && productOrderModel.getProduct().getPrice() != null) {
            Double price = productOrderModel.getProduct().getPrice();
            return BigDecimal.valueOf(price);
        }

        return BigDecimal.ZERO;
    }

    private double calculateOrderPrice(List<ProductOrderModel> productOrders) {
        if (productOrders == null || productOrders.isEmpty()) return 0.0;

        BigDecimal total = BigDecimal.ZERO;
        List<BigDecimal> startersPrices = new ArrayList<>();
        int segundosUnits = 0;

        for (ProductOrderModel productOrderModel : productOrders) {
            if (productOrderModel == null) continue;

            final int qty = productOrderModel.getQuantity() == null ? 0 : productOrderModel.getQuantity();
            final BigDecimal price = resolveUnitPrice(productOrderModel);
            final String typeName = productOrderModel.getProduct() != null && productOrderModel.getProduct().getProductType() != null
                    ? Optional.ofNullable(productOrderModel.getProduct().getProductType().getName()).orElse("")
                    : "";

            if (ProductTypeEnum.SEGUNDOS.name().equalsIgnoreCase(typeName)) {
                segundosUnits += qty;
                total = total.add(price.multiply(BigDecimal.valueOf(qty)));
            } else if (ProductTypeEnum.ENTRADAS.name().equalsIgnoreCase(typeName)) {
                startersPrices.addAll(Collections.nCopies(qty, price));
            } else {
                total = total.add(price.multiply(BigDecimal.valueOf(qty)));
            }
        }

        startersPrices.sort(Comparator.reverseOrder());
        final int startersToCollect = Math.max(0, startersPrices.size() - segundosUnits);
        for (int i = 0; i < startersToCollect; i++) {
            total = total.add(startersPrices.get(i));
        }

        return total.doubleValue();
    }

    @Override
    @Transactional
    public OrderResponseDto create(OrderRequestDto order) {
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

        productOrderModel.forEach(productOrder -> productOrder.setOrder(orderModel));

        orderEmployeeModel.forEach(orderEmployee -> orderEmployee.setOrder(orderModel));

        TableModel table = tableService.findById(orderModel.getTable().getId());

        this.tableService.changeStatus(table.getId(), TableStatus.OCCUPIED);

        double totalPrice = this.calculateOrderPrice(orderModel.getProductOrders());

        orderModel.setTotalPrice(totalPrice);
        orderModel.setPaid(false);

        OrderModel saved = orderRepository.save(orderModel);

        return orderMapper.toDto(saved);
    }

    @Transactional
    public void allServeProductOrders(UUID orderId) {
        OrderModel order = this.findModelById(orderId);

        for (ProductOrderModel po : order.getProductOrders()) {
            po.setServedQuantity(po.getQuantity());
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderResponseDto serveProductOrder(ServeProductOrderRequestDto serveProductOrderRequestDto) {
        UUID orderId = serveProductOrderRequestDto.getOrderId();
        long productOrderId = serveProductOrderRequestDto.getProductOrderId();
        int quantityServe = serveProductOrderRequestDto.getQuantity();

        if (quantityServe == 0) {
            throw new IllegalArgumentException("Quantity cannot be 0");
        }

        OrderModel order = this.findModelById(orderId);

        if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException("Cannot modify items for a " + order.getOrderStatus() + " order");
        }

        ProductOrderModel po = order.getProductOrders()
                .stream()
                .filter(p -> p.getId() != null && p.getId().equals(productOrderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ProductOrder with id " + productOrderId + " not found in order " + orderId));

        int newServedQuantity = getNewServedQuantity(po, quantityServe);

        po.setServedQuantity(newServedQuantity);

        OrderModel saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }

    private static int getNewServedQuantity(ProductOrderModel po, int quantityServe) {
        int currentServed = po.getServedQuantity() == null ? 0 : po.getServedQuantity();
        int totalQuantity = po.getQuantity() == null ? 0 : po.getQuantity();
        int newServedQuantity = currentServed + quantityServe;

        if (newServedQuantity < 0) {
            throw new IllegalArgumentException(
                    String.format("Cannot unserve %d items. Only %d items are currently marked as served",
                            Math.abs(quantityServe), currentServed)
            );
        }

        if (newServedQuantity > totalQuantity) {
            int remaining = totalQuantity - currentServed;
            throw new IllegalArgumentException(
                    String.format("Cannot serve %d items. Only %d remaining out of %d total (Already served: %d)",
                            quantityServe, remaining, totalQuantity, currentServed)
            );
        }
        return newServedQuantity;
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderResponseDto> findAll(PaginationRequestDto requestDto, OrderStatus status) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities;

        if (status != null) {
            entities = orderRepository.findAllByOrderStatus(pageable, status);
        } else {
            entities = orderRepository.findAll(pageable);
        }

        final List<OrderResponseDto> orderResponseDtos = orderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderResponseDto> findAllByArrayStatus(PaginationRequestDto requestDto,
                                                                        List<OrderStatus> ordersStatus) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByOrderStatusIn(pageable, ordersStatus);
        final List<OrderResponseDto> orderResponseDtos = orderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderResponseDto> findAllByTablesAndStatus(PaginationRequestDto requestDto,
                                                                            List<Long> tableIds,
                                                                            List<OrderStatus> ordersStatus) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByTableIdInAndOrderStatusIn(pageable, tableIds, ordersStatus);
        final List<OrderResponseDto> orderResponseDtos = orderMapper.toDto(entities.getContent());
        return new PaginationResponseDto<>(
                orderResponseDtos,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber() + 1,
                entities.isEmpty()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginationResponseDto<OrderResponseDto> findAllByTableId(PaginationRequestDto requestDto, Long tableId) {
        final Pageable pageable = PaginationUtils.getPageable(requestDto);
        final Page<OrderModel> entities = orderRepository.findAllByTableId(pageable, tableId);
        final List<OrderResponseDto> orderDtos = orderMapper.toDto(entities.getContent()).stream()
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
    public OrderResponseDto findById(UUID id) {
        OrderModel order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByTableIds(List<Long> tableIds) {
        List<OrderModel> order = orderRepository
                .findByTableIdIn(tableIds)
                .orElseThrow(() -> new ResourceNotFoundException("No pending orders found for the given table IDs"));

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderModel findModelById(UUID id) {
        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public OrderResponseDto update(UUID id, OrderRequestDto order) {
        OrderModel orderModel = this.findModelById(id);

        List<ProductOrderRequestDto> productOrderDtos = order.getProductOrders();
        List<ProductOrderModel> incoming = productOrderMapper.toModel(productOrderDtos);

        IntStream.range(0, incoming.size()).forEachOrdered(i -> {
            ProductOrderModel po = incoming.get(i);
            Long productId = productOrderDtos.get(i).getProductId();
            ProductModel product = productService.findModelById(productId);
            po.setProduct(product);
            if (po.getUnitPrice() == null) po.setUnitPrice(product.getPrice());
            if (po.getSubtotal() == null && po.getQuantity() != null && po.getUnitPrice() != null) {
                po.setSubtotal(po.getQuantity() * po.getUnitPrice());
            }
        });

        List<ProductOrderModel> existing = Optional.ofNullable(orderModel.getProductOrders()).orElseGet(() -> {
            List<ProductOrderModel> list = new ArrayList<>();
            orderModel.setProductOrders(list);
            return list;
        });

        Set<Long> incomingIds = incoming.stream()
                .map(p -> p.getProduct() == null ? null : p.getProduct().getId())
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());

        Iterator<ProductOrderModel> it = existing.iterator();
        while (it.hasNext()) {
            ProductOrderModel ex = it.next();
            Long exPid = ex.getProduct() == null ? null : ex.getProduct().getId();
            if (exPid == null || !incomingIds.contains(exPid)) {
                it.remove();
            } else {
                ProductOrderModel matched = incoming.stream()
                        .filter(p -> p.getProduct() != null && p.getProduct().getId() != null && p.getProduct().getId().equals(exPid))
                        .findFirst()
                        .orElse(null);
                if (matched != null) {
                    ex.setQuantity(matched.getQuantity());
                    ex.setUnitPrice(matched.getUnitPrice());
                    ex.setSubtotal(matched.getSubtotal());
                    ex.setOrder(orderModel);
                }
            }
        }

        for (ProductOrderModel inc : incoming) {
            Long pid = inc.getProduct() == null ? null : inc.getProduct().getId();
            boolean exists = existing.stream()
                    .anyMatch(e -> e.getProduct() != null && e.getProduct().getId() != null && e.getProduct().getId().equals(pid));
            if (!exists) {
                inc.setOrder(orderModel);
                existing.add(inc);
            }
        }

        int totalItems = existing.stream()
                .mapToInt(p -> p.getQuantity() == null ? 0 : p.getQuantity())
                .sum();
        orderModel.setTotalItems(totalItems);

        double totalPrice = this.calculateOrderPrice(existing);
        orderModel.setTotalPrice(totalPrice);

        orderModel.setOrderStatus(order.getOrderStatus());
        orderModel.setComment(order.getComment());
        orderModel.setPaid(order.getPaid());

        OrderModel saved = orderRepository.save(orderModel);
        return orderMapper.toDto(saved);
    }

    @Override
    @Transactional
    public OrderResponseDto changeStatus(@NonNull ChangeOrderStatusDto changeOrderStatusDto) {
        OrderModel order = this.findModelById(changeOrderStatusDto.getOrderId());

        OrderStatus newStatus = changeOrderStatusDto.getStatus();

        if (newStatus.equals(OrderStatus.READY)) {
            order.setOrderStatus(newStatus);
            this.allServeProductOrders(order.getId());
        }
        else if (newStatus.equals(OrderStatus.PAID)) {
            order.setOrderStatus(newStatus);
            order.setPaid(true);
            order.setPaidAt(Instant.now());
            tableService.changeStatus(order.getTable().getId(), TableStatus.AVAILABLE);
            return orderMapper.toDto(orderRepository.save(order));
        } else if (newStatus.equals(OrderStatus.COMPLETED)) {
            tableService.changeStatus(order.getTable().getId(), TableStatus.AVAILABLE);
        }

        order.setOrderStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public String delete(UUID id) {
        this.findModelById(id);
//        orderRepository.deleteById(id);

        return "Deleted order with id: " + id;
    }
}