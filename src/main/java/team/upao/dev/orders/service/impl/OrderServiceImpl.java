package team.upao.dev.orders.service.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team.upao.dev.common.dto.PaginationRequestDto;
import team.upao.dev.common.dto.PaginationResponseDto;
import team.upao.dev.common.utils.PaginationUtils;
import team.upao.dev.employees.services.EmployeeService;
import team.upao.dev.exceptions.ResourceNotFoundException;
import team.upao.dev.inventory.dto.ProductInventoryResponseDto;
import team.upao.dev.inventory.service.InventoryService;
import team.upao.dev.inventory.service.ProductInventoryService;
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

    /**
     * Valida que haya stock suficiente para todos los productos de una orden.
     * Se llama ANTES de crear la orden para evitar pedidos que no se puedan completar.
     */
    private void validateStockForOrder(List<ProductOrderRequestDto> productOrders) {
        log.info("Validando stock para {} productos en la orden", productOrders.size());
        
        // Agregar cantidades por producto (en caso de duplicados)
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (ProductOrderRequestDto po : productOrders) {
            productQuantities.merge(po.getProductId(), po.getQuantity(), Integer::sum);
        }
        
        // Validar cada producto y recolectar errores
        StringBuilder allErrors = new StringBuilder();
        
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            Integer quantity = entry.getValue();
            
            ProductModel product = productService.findModelById(productId);
            
            // Obtener detalle del error de stock
            String stockError = productInventoryService.getStockErrorDetail(productId, BigDecimal.valueOf(quantity));
            
            if (stockError != null) {
                log.error("Stock insuficiente para producto: {} (ID: {}), cantidad solicitada: {}", 
                    product.getName(), productId, quantity);
                
                if (allErrors.length() > 0) {
                    allErrors.append(" | ");
                }
                allErrors.append(String.format("%s (%d unidades): %s", 
                    product.getName(), quantity, stockError));
            }
        }
        
        if (allErrors.length() > 0) {
            throw new IllegalArgumentException(
                "Stock insuficiente para completar el pedido. " + allErrors.toString()
            );
        }
        
        log.info("Validación de stock completada exitosamente");;
    }

    @Override
    @Transactional
    public OrderResponseDto create(OrderRequestDto order) {
        // Validar stock ANTES de crear la orden
        validateStockForOrder(order.getProductOrders());
        
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

        // Descontar inventario al crear la orden
        this.deductInventoryForOrder(saved);

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
    @Transactional(readOnly = true)
    public OrderModel findModelByOrderIdInPayment(UUID id) {
        return orderRepository.findByOrderIdInPaymentModel(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
    }

    @Override
    @Transactional
    public OrderResponseDto update(UUID id, OrderRequestDto order) {
        OrderModel orderModel = this.findModelById(id);

        // Bloquear modificaciones si el pedido ya está en READY o estados posteriores
        OrderStatus currentStatus = orderModel.getOrderStatus();
        if (currentStatus == OrderStatus.READY || 
            currentStatus == OrderStatus.PAID || 
            currentStatus == OrderStatus.COMPLETED || 
            currentStatus == OrderStatus.CANCELLED) {
            throw new IllegalArgumentException(
                String.format("No se puede modificar un pedido en estado %s. El pedido ya fue preparado o finalizado.", 
                    currentStatus)
            );
        }

        // Guardar estado anterior de productos para calcular diferencias de inventario
        Map<Long, Integer> previousQuantities = new HashMap<>();
        if (orderModel.getProductOrders() != null) {
            for (ProductOrderModel po : orderModel.getProductOrders()) {
                if (po.getProduct() != null && po.getProduct().getId() != null) {
                    previousQuantities.put(po.getProduct().getId(), 
                        po.getQuantity() == null ? 0 : po.getQuantity());
                }
            }
        }

        List<ProductOrderRequestDto> productOrderDtos = order.getProductOrders();
        List<ProductOrderModel> incoming = productOrderMapper.toModel(productOrderDtos);

        // Calcular nuevas cantidades para validar stock
        Map<Long, Integer> newQuantities = new HashMap<>();
        IntStream.range(0, incoming.size()).forEachOrdered(i -> {
            ProductOrderModel po = incoming.get(i);
            Long productId = productOrderDtos.get(i).getProductId();
            ProductModel product = productService.findModelById(productId);
            po.setProduct(product);
            if (po.getUnitPrice() == null) po.setUnitPrice(product.getPrice());
            if (po.getSubtotal() == null && po.getQuantity() != null && po.getUnitPrice() != null) {
                po.setSubtotal(po.getQuantity() * po.getUnitPrice());
            }
            newQuantities.put(productId, po.getQuantity() == null ? 0 : po.getQuantity());
        });

        // Validar stock solo para incrementos (diferencia positiva)
        validateStockForOrderUpdate(previousQuantities, newQuantities);

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

        // Mantener el estado actual si no se envía uno nuevo
        if (order.getOrderStatus() != null) {
            orderModel.setOrderStatus(order.getOrderStatus());
        }
        // Si es null, mantiene el estado actual (no lo cambiamos)
        
        orderModel.setComment(order.getComment());

        OrderModel saved = orderRepository.save(orderModel);

        // Ajustar inventario según las diferencias
        adjustInventoryForOrderUpdate(previousQuantities, newQuantities);

        return orderMapper.toDto(saved);
    }

    /**
     * Valida que haya stock suficiente para los incrementos en la orden.
     * Solo valida productos nuevos o con cantidad aumentada.
     */
    private void validateStockForOrderUpdate(Map<Long, Integer> previousQuantities, 
                                              Map<Long, Integer> newQuantities) {
        log.info("Validando stock para actualización de orden");
        
        StringBuilder allErrors = new StringBuilder();
        
        for (Map.Entry<Long, Integer> entry : newQuantities.entrySet()) {
            Long productId = entry.getKey();
            int newQty = entry.getValue();
            int prevQty = previousQuantities.getOrDefault(productId, 0);
            int difference = newQty - prevQty;
            
            // Solo validar si hay incremento
            if (difference > 0) {
                ProductModel product = productService.findModelById(productId);
                
                String stockError = productInventoryService.getStockErrorDetail(productId, BigDecimal.valueOf(difference));
                
                if (stockError != null) {
                    log.error("Stock insuficiente para incremento de producto: {} (ID: {}), " +
                        "cantidad adicional solicitada: {}", product.getName(), productId, difference);
                    
                    if (allErrors.length() > 0) {
                        allErrors.append(" | ");
                    }
                    allErrors.append(String.format("%s (+%d): %s", 
                        product.getName(), difference, stockError));
                }
            }
        }
        
        if (allErrors.length() > 0) {
            throw new IllegalArgumentException(
                "Stock insuficiente para modificar el pedido. " + allErrors.toString()
            );
        }
        
        log.info("Validación de stock para actualización completada");
    }

    /**
     * Ajusta el inventario según los cambios en la orden.
     * - Restaura stock para productos eliminados o con cantidad reducida
     * - Deduce stock para productos nuevos o con cantidad aumentada
     */
    private void adjustInventoryForOrderUpdate(Map<Long, Integer> previousQuantities, 
                                                Map<Long, Integer> newQuantities) {
        log.info("Ajustando inventario para actualización de orden");
        
        // Procesar todos los productos (anteriores y nuevos)
        Set<Long> allProductIds = new java.util.HashSet<>();
        allProductIds.addAll(previousQuantities.keySet());
        allProductIds.addAll(newQuantities.keySet());
        
        for (Long productId : allProductIds) {
            int prevQty = previousQuantities.getOrDefault(productId, 0);
            int newQty = newQuantities.getOrDefault(productId, 0);
            int difference = newQty - prevQty;
            
            if (difference == 0) {
                continue; // Sin cambios
            }
            
            List<ProductInventoryResponseDto> recipe = productInventoryService.getRecipeByProductId(productId);
            
            if (recipe.isEmpty()) {
                log.debug("Producto {} no tiene receta (puede ser bebida/descartable con inventario directo)", productId);
                continue;
            }
            
            for (ProductInventoryResponseDto ingredient : recipe) {
                BigDecimal quantityPerUnit = ingredient.getQuantity();
                BigDecimal totalChange = quantityPerUnit.multiply(BigDecimal.valueOf(Math.abs(difference)));
                
                if (difference > 0) {
                    // Incremento: descontar del inventario
                    log.info("Descontando {} {} de {} (incremento de {} unidades de producto)", 
                        totalChange, ingredient.getUnitOfMeasure(), 
                        ingredient.getInventoryName(), difference);
                    
                    inventoryService.deductStock(
                        ingredient.getInventoryId(),
                        totalChange,
                        ingredient.getUnitOfMeasure()
                    );
                } else {
                    // Decremento: restaurar al inventario
                    log.info("Restaurando {} {} de {} (reducción de {} unidades de producto)", 
                        totalChange, ingredient.getUnitOfMeasure(), 
                        ingredient.getInventoryName(), Math.abs(difference));
                    
                    inventoryService.restoreStock(
                        ingredient.getInventoryId(),
                        totalChange,
                        ingredient.getUnitOfMeasure()
                    );
                }
            }
        }
        
        log.info("Ajuste de inventario completado");
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
            // Ya no descontamos aquí - se descuenta al crear la orden
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

    @Transactional
    private void deductInventoryForOrder(OrderModel order) {
        log.info("Iniciando deducción de inventario para orden ID: {}", order.getId());
    
     try{
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
                // 🔴 LOG IMPORTANTE: imprimir la receta completa
                log.info(
                    "Receta para producto {} ({}): {} ingrediente(s)",
                    productId,
                    productOrder.getProduct().getName(),
                    recipe.size()
                );


                // Descontar cada ingrediente
                for (ProductInventoryResponseDto ingredient : recipe) {
                    // Calcular cantidad a deducir
                    java.math.BigDecimal quantityToDeduct = ingredient.getQuantity()
                        .multiply(java.math.BigDecimal.valueOf(quantityOrdered));
                    
                    log.info("Deducido {} {} de {} para la orden {}", 
                        quantityToDeduct, ingredient.getUnitOfMeasure(), 
                        ingredient.getInventoryName(), order.getId());
                    
                    inventoryService.deductStock(
                        ingredient.getInventoryId(),
                        quantityToDeduct,
                        ingredient.getUnitOfMeasure()
                    );
                }
            }
            log.info("Deducción completada exitosamente para orden: {}", order.getId());
            
        } catch (IllegalArgumentException e) {
            log.error("Error en deducción de inventario: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado en deducción: {}", e.getMessage());
            throw new RuntimeException("Error al procesar deducción de inventario", e);
        }
    }
}
