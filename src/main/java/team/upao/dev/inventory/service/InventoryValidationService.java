package team.upao.dev.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.upao.dev.common.utils.UnitConverter;
import team.upao.dev.inventory.enums.InventoryType;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.model.ProductInventoryModel;

import java.math.BigDecimal;

@Slf4j
@Service
public class InventoryValidationService {
    
    /**
     * Valida que una cantidad sea válida (no nula, positiva)
     */
    public void validateQuantity(BigDecimal quantity) {
        UnitConverter.validateQuantity(quantity);
    }
    
    /**
     * Valida que una unidad sea válida
     */
    public void validateUnit(UnitOfMeasure unit) {
        if (unit == null) {
            throw new IllegalArgumentException("La unidad de medida no puede ser nula");
        }
        log.debug("Unidad válida: {}", unit.getSymbol());
    }
    
    public void validateTypeAndUnitConsistency(InventoryType type, UnitOfMeasure unit, BigDecimal quantity) {
        log.debug("Validando reglas de negocio: Tipo={}, Unidad={}, Cantidad={}", 
            type.name(), unit.getSymbol(), quantity);
        
        if ((type == InventoryType.BEVERAGE || type == InventoryType.DISPOSABLE) 
            && unit != UnitOfMeasure.UNIDAD) {
            
            log.error("❌ VIOLACIÓN: {} no puede usar unidad {}. Solo permite UNIDAD", 
                type.name(), unit.getSymbol());
            
            throw new IllegalArgumentException(
                String.format(
                    "%s SOLO puede usar unidad de medida 'UNIDAD'. Se intentó usar '%s'",
                    type.name(), unit.getSymbol()
                )
            );
        }
        
        if (unit == UnitOfMeasure.UNIDAD && this.hasDecimals(quantity)) {
            log.error("VIOLACIÓN: Unidad 'UNIDAD' no permite decimales. Recibido: {}", quantity);
            
            throw new IllegalArgumentException(
                String.format(
                    "La unidad de medida 'UNIDAD' SOLO acepta cantidades enteras. Recibido: %s (tiene decimales)",
                    quantity
                )
            );
        }
        
        log.debug("Reglas de negocio validadas correctamente");
    }


    public void validateTypeChange(InventoryType oldType, InventoryType newType, 
                                   UnitOfMeasure currentUnit, BigDecimal quantity) {
        log.debug("Validando cambio de tipo: {} → {}", oldType.name(), newType.name());
        
        // Si cambias a BEVERAGE o DISPOSABLE, DEBE ser UNIDAD y cantidad entera
        if ((newType == InventoryType.BEVERAGE || newType == InventoryType.DISPOSABLE)) {
            if (currentUnit != UnitOfMeasure.UNIDAD) {
                log.error("No se puede cambiar a {} con unidad {}", newType.name(), currentUnit.getSymbol());
                throw new IllegalArgumentException(
                    String.format(
                        "No se puede cambiar a %s con unidad %s. Debe ser UNIDAD",
                        newType.name(), currentUnit.getSymbol()
                    )
                );
            }
            
            if (this.hasDecimals(quantity)) {
                log.error("No se puede cambiar a {} con cantidad decimal {}", newType.name(), quantity);
                throw new IllegalArgumentException(
                    String.format(
                        "No se puede cambiar a %s con cantidad decimal %s. Debe ser entero",
                        newType.name(), quantity
                    )
                );
            }
        }
        
        log.debug("Cambio de tipo validado");
    }

    public void validateUnitChange(InventoryType type, UnitOfMeasure oldUnit, 
                                   UnitOfMeasure newUnit, BigDecimal quantity) {
        log.debug("Validando cambio de unidad: {} → {}", oldUnit.getSymbol(), newUnit.getSymbol());
        
        // BEVERAGE/DISPOSABLE no pueden cambiar a otra unidad que no sea UNIDAD
        if ((type == InventoryType.BEVERAGE || type == InventoryType.DISPOSABLE)
            && newUnit != UnitOfMeasure.UNIDAD) {
            
            log.error("❌ {} no permite cambiar a unidad {}", type.name(), newUnit.getSymbol());
            throw new IllegalArgumentException(
                String.format(
                    "%s SOLO permite unidad 'UNIDAD'. No se puede cambiar a '%s'",
                    type.name(), newUnit.getSymbol()
                )
            );
        }
        
        // Si cambias a UNIDAD, cantidad debe ser entera
        if (newUnit == UnitOfMeasure.UNIDAD && this.hasDecimals(quantity)) {
            log.error("❌ Al cambiar a UNIDAD, cantidad debe ser entera. Recibido: {}", quantity);
            throw new IllegalArgumentException(
                String.format(
                    "Al cambiar a unidad 'UNIDAD', la cantidad debe ser entera. Recibido: %s",
                    quantity
                )
            );
        }
        
        log.debug("✅ Cambio de unidad validado");
    }

    /**
     * Valida que el inventario tenga stock suficiente
     */
    public void validateSufficientStock(InventoryModel inventory, BigDecimal requiredQuantity, UnitOfMeasure requiredUnit) {
        if (inventory.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Inventario agotado: {} (Stock: {})", 
                inventory.getName(), inventory.getQuantity());
            throw new IllegalArgumentException(
                String.format("El inventario de '%s' está agotado", inventory.getName())
            );
        }
        
        // Convertir cantidad requerida a unidad del inventario
        BigDecimal requiredInInventoryUnit = UnitConverter.convert(
            requiredQuantity, 
            requiredUnit, 
            inventory.getUnitOfMeasure()
        );
        
        log.debug(
            "Comparando stock: Disponible={} {}, Requerido={} {} (convertido de {} {})",
            inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol(),
            requiredInInventoryUnit, inventory.getUnitOfMeasure().getSymbol(),
            requiredQuantity, requiredUnit.getSymbol()
        );

        if (inventory.getQuantity().compareTo(requiredInInventoryUnit) < 0) {
            log.error(
                "Stock insuficiente: {} | Disponible: {} {} | Requerido: {} {}",
                inventory.getName(),
                inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol(),
                requiredQuantity, requiredUnit.getSymbol()
            );
            throw new IllegalArgumentException(
                String.format(
                    "Stock insuficiente de '%s'. Disponible: %s %s, Requerido: %s %s",
                    inventory.getName(),
                    inventory.getQuantity(), inventory.getUnitOfMeasure().getSymbol(),
                    requiredQuantity, requiredUnit.getSymbol()
                )
            );
        }
        
        log.debug("Stock suficiente para: {}", inventory.getName());
    }
    
    /**
     * Valida compatibilidad entre unidades de receta e inventario
     */
    public void validateUnitCompatibility(ProductInventoryModel recipe, InventoryModel inventory) {
        if (!recipe.getUnitOfMeasure().isCompatible(inventory.getUnitOfMeasure())) {
            log.error(
                "Unidades incompatibles: Receta='{}' {} | Inventario='{}' {}",
                recipe.getProduct().getName(), recipe.getUnitOfMeasure().getSymbol(),
                inventory.getName(), inventory.getUnitOfMeasure().getSymbol()
            );
            throw new IllegalArgumentException(
                String.format(
                    "Unidades incompatibles en receta '%s': " +
                    "Receta usa %s pero inventario de '%s' usa %s. " +
                    "Unidades válidas: %s",
                    recipe.getProduct().getName(),
                    recipe.getUnitOfMeasure().getSymbol(),
                    inventory.getName(),
                    inventory.getUnitOfMeasure().getSymbol(),
                    recipe.getUnitOfMeasure().getCompatibleUnits()
                )
            );
        }
    }
    
    /**
     * Valida nombre único en inventario
     */
    public void validateUniqueInventoryName(String name, boolean nameExists) {
        if (nameExists) {
            log.warn("Nombre duplicado en inventario: {}", name);
            throw new IllegalArgumentException(
                String.format("El ítem '%s' ya existe en inventario", name)
            );
        }
        log.debug("Nombre único: {}", name);
    }

    private boolean hasDecimals(BigDecimal value) {
        if (value == null) return false;
        return value.scale() > 0 && value.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) != 0;
    }
}