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
import team.upao.dev.exceptions.NotFoundException;
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
import team.upao.dev.products.enums.ProductOrderItemStatus;
import team.upao.dev.products.enums.ProductOrderStatus;
import team.upao.dev.products.enums.ProductTypeEnum;
import team.upao.dev.products.mapper.ProductOrderMapper;
import team.upao.dev.products.model.ProductModel;
import team.upao.dev.products.model.ProductOrderItemModel;
import team.upao.dev.products.model.ProductOrderModel;
import team.upao.dev.products.service.ProductService;
import team.upao.dev.tables.enums.TableStatus;
import team.upao.dev.tables.model.TableModel;
import team.upao.dev.tables.service.TableService;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;  
import team.upao.dev.inventory.service.ProductInventoryService;  
import team.upao.dev.inventory.service.InventoryService; 

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

    // NUEVOS INYECTADOS
    private final ProductInventoryService productInventoryService;
    private final InventoryService inventoryService;

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

    @Override
    @Transactional
    public OrderResponseDto serveProductOrder(ServeProductOrderRequestDto serveProductOrderRequestDto) {
        long orderId = serveProductOrderRequestDto.getOrderId();
        long productOrderId = serveProductOrderRequestDto.getProductOrderId();
        int quantityToServe = serveProductOrderRequestDto.getQuantity();

        if (quantityToServe <= 0) throw new IllegalArgumentException("Quantity to serve must be > 0");

        OrderModel order = this.findModelById(orderId);

        ProductOrderModel po = order.getProductOrders()
                .stream()
                .filter(p -> p.getId() != null && p.getId().equals(productOrderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("ProductOrder not found"));

        if (po.getItems() == null || po.getItems().isEmpty()) {
            ProductOrderItemModel base = ProductOrderItemModel.builder()
                    .quantity(po.getQuantity() == null ? 0 : po.getQuantity())
                    .preparedQuantity(0)
                    .servedQuantity(0)
                    .status(ProductOrderItemStatus.PENDING)
                    .productOrder(po)
                    .build();
            po.setItems(new ArrayList<>(List.of(base)));
        }

        int remainingToServe = quantityToServe;

        for (ProductOrderItemModel item : po.getItems()) {
            if (remainingToServe <= 0) break;

            int itemRemaining = (item.getQuantity() == null ? 0 : item.getQuantity()) - (item.getServedQuantity() == null ? 0 : item.getServedQuantity());
            if (itemRemaining <= 0) continue;

            int serve = Math.min(itemRemaining, remainingToServe);
            item.setServedQuantity((item.getServedQuantity() == null ? 0 : item.getServedQuantity()) + serve);
            remainingToServe -= serve;

            if (item.getServedQuantity() >= item.getQuantity()) {
                item.setStatus(ProductOrderItemStatus.SERVED);
            } else {
                item.setStatus(ProductOrderItemStatus.PREPARING);
            }
        }

        if (remainingToServe > 0) {
            throw new IllegalArgumentException("Not enough remaining quantity to serve. Remaining: " + remainingToServe);
        }

        int totalServed = po.getItems().stream().mapToInt(i -> i.getServedQuantity() == null ? 0 : i.getServedQuantity()).sum();
        int totalQty = po.getQuantity() == null ? 0 : po.getQuantity();

        if (totalServed >= totalQty && totalQty > 0) {
            po.setStatus(ProductOrderStatus.SERVED);
        } else if (totalServed > 0) {
            po.setStatus(ProductOrderStatus.PREPARING);
        } else {
            po.setStatus(ProductOrderStatus.PENDING);
        }

        boolean allServed = order.getProductOrders()
                .stream()
                .allMatch(p -> {
                    int served = p.getItems() == null ? 0 : p.getItems().stream().mapToInt(i -> i.getServedQuantity() == null ? 0 : i.getServedQuantity()).sum();
                    int qty = p.getQuantity() == null ? 0 : p.getQuantity();
                    return qty > 0 && served >= qty;
                });

//        if (allServed) {
//            order.setOrderStatus(OrderStatus.COMPLETED);
//            if (order.getTable() != null) {
//                tableService.changeStatus(order.getTable().getId(), TableStatus.AVAILABLE);
//            }
//        }

        OrderModel saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
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
    public OrderResponseDto findById(Long id) {
        OrderModel order = orderRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + id));

        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> findByTableIds(List<Long> tableIds) {
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
    public OrderResponseDto update(Long id, OrderRequestDto order) {
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

        java.util.Set<Long> incomingIds = incoming.stream()
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

        if (newStatus.equals(OrderStatus.PAID)) {
            order.setOrderStatus(newStatus);
            order.setPaid(true);
            order.setPaidAt(Instant.now());
            return orderMapper.toDto(orderRepository.save(order));
        } else if (newStatus.equals(OrderStatus.COMPLETED)) {
            this.deductInventoryForOrder(order);// HU5: DEDUCIR STOCK AL COMPLETAR ORDEN
            tableService.changeStatus(order.getTable().getId(), TableStatus.AVAILABLE);
        }

        order.setOrderStatus(newStatus);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public String delete(Long id) {
        OrderModel order = this.findModelById(id);
//        orderRepository.deleteById(id);

        return "Deleted order with id: " + id;
    }

    private void deductInventoryForOrder(OrderModel order) {
        log.info("Iniciando deducción de inventario para orden ID: {}", order.getId());
        
        // Validar stock antes de procesar
        for (ProductOrderModel productOrder : order.getProductOrders()) {
            Long productId = productOrder.getProduct().getId();
            Integer quantity = productOrder.getQuantity();
            
            // Verificar si el producto puede venderse (tiene suficiente stock de ingredientes)
            if (!productInventoryService.canSellProduct(productId, java.math.BigDecimal.valueOf(quantity))) {
                log.error("Stock insuficiente para producto ID: {}", productId);
                throw new IllegalArgumentException(
                    String.format("Stock insuficiente de ingredientes para el producto: %s", 
                        productOrder.getProduct().getName())
                );
            }
        }
        
        // Si todo está ok, descontar
        for (ProductOrderModel productOrder : order.getProductOrders()) {
            Long productId = productOrder.getProduct().getId();
            Integer quantityOrdered = productOrder.getQuantity();
            
            // Obtener receta del producto
            List<ProductInventoryResponseDto> recipe = productInventoryService.getRecipeByProductId(productId);
            
            // Descontar cada ingrediente
            for (ProductInventoryResponseDto ingredient : recipe) {
                java.math.BigDecimal quantityToDeduct = ingredient.getQuantity()
                    .multiply(java.math.BigDecimal.valueOf(quantityOrdered));
                
                inventoryService.deductStock(ingredient.getInventoryId(), quantityToDeduct);
                log.info("Deducido {} {} de {} para la orden {}", 
                    quantityToDeduct, ingredient.getUnitOfMeasure(), 
                    ingredient.getInventoryName(), order.getId());
            }
        }
        
        log.info("Deducción de inventario completada para orden ID: {}", order.getId());
    }
}
