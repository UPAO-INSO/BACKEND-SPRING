package team.upao.dev.inventory.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import team.upao.dev.common.utils.UnitConverter;
import team.upao.dev.inventory.enums.UnitOfMeasure;
import team.upao.dev.inventory.model.InventoryModel;
import team.upao.dev.inventory.model.ProductInventoryModel;

import java.math.BigDecimal;

/**
 * Servicio para manejar conversiones de unidades
 * - Convertir cantidades entre unidades compatibles
 * - Calcular cantidades en unidades del inventario
 * - Aplicar conversiones en deducciones
 */
@Slf4j
@Service
public class InventoryConversionService {
    
    /**
     * Convierte una cantidad de una unidad a otra
     */
    public BigDecimal convert(BigDecimal quantity, UnitOfMeasure fromUnit, UnitOfMeasure toUnit) {
        log.debug("Convirtiendo: {} {} → {}", quantity, fromUnit.getSymbol(), toUnit.getSymbol());
        
        BigDecimal converted = UnitConverter.convert(quantity, fromUnit, toUnit);
        
        log.info(" Conversión completada: {} {} = {} {}", 
            quantity, fromUnit.getSymbol(), converted, toUnit.getSymbol());
        
        return converted;
    }
    
    /**
     * Obtiene la cantidad que necesita convertirse a unidad del inventario
     */
    public BigDecimal getQuantityInInventoryUnit(ProductInventoryModel recipe, InventoryModel inventory) {
        log.debug(
            "Calculando cantidad en unidad del inventario: {} {} → {}",
            recipe.getQuantity(), recipe.getUnitOfMeasure().getSymbol(),
            inventory.getUnitOfMeasure().getSymbol()
        );
        
        BigDecimal converted = UnitConverter.convert(
            recipe.getQuantity(),
            recipe.getUnitOfMeasure(),
            inventory.getUnitOfMeasure()
        );
        
        log.info(
            "✅ Cantidad convertida: {} {} = {} {}",
            recipe.getQuantity(), recipe.getUnitOfMeasure().getSymbol(),
            converted, inventory.getUnitOfMeasure().getSymbol()
        );
        
        return converted;
    }
    
    /**
     * Calcula cantidad total a deducir considerando la receta y cantidad ordenada
     */
    public BigDecimal calculateTotalDeductionQuantity(
            BigDecimal recipeQuantity, 
            BigDecimal quantityOrdered, 
            UnitOfMeasure recipeUnit,
            InventoryModel inventory) {
        
        log.debug(
            "Calculando deducción total: {} {} × {} unidades = ? {}",
            recipeQuantity, recipeUnit.getSymbol(),
            quantityOrdered, inventory.getUnitOfMeasure().getSymbol()
        );
        
        // Multiplicar cantidad de receta por cantidad ordenada
        BigDecimal totalNeeded = recipeQuantity.multiply(quantityOrdered);
        
        log.debug("Total necesario (antes de conversión): {} {}", totalNeeded, recipeUnit.getSymbol());
        
        // Convertir a unidad del inventario
        BigDecimal totalInInventoryUnit = UnitConverter.convert(
            totalNeeded,
            recipeUnit,
            inventory.getUnitOfMeasure()
        );
        
        log.info(
            "Deducción calculada: {} {} = {} {} (para {} unidades)",
            totalNeeded, recipeUnit.getSymbol(),
            totalInInventoryUnit, inventory.getUnitOfMeasure().getSymbol(),
            quantityOrdered
        );
        
        return totalInInventoryUnit;
    }
    
    /**
     * Valida y convierte una cantidad entre unidades
     */
    public BigDecimal validateAndConvert(
            BigDecimal quantity, 
            UnitOfMeasure sourceUnit,
            UnitOfMeasure targetUnit,
            String context) {
        
        log.debug("Validando conversión en contexto: {}", context);
        
        UnitConverter.validateQuantity(quantity);
        UnitConverter.validateUnitCompatibility(sourceUnit, targetUnit);
        
        return this.convert(quantity, sourceUnit, targetUnit);
    }
}