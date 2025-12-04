package team.upao.dev.common.utils;

import team.upao.dev.inventory.enums.UnitOfMeasure;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;

@Slf4j
public class UnitConverter {
    public static BigDecimal convert(BigDecimal quantity, UnitOfMeasure fromUnit, UnitOfMeasure toUnit) {
        // Validar que quantity no sea nula o negativa
        validateQuantity(quantity);
        
        // Validar que las unidades sean compatibles
        validateUnitCompatibility(fromUnit, toUnit);
        
        // Convertir la cantidad de la unidad de origen a la unidad de destino
        BigDecimal converted = fromUnit.convertTo(quantity, toUnit);
        
        log.debug("Conversión: {} {} = {} {}", 
            quantity, fromUnit.getSymbol(), converted, toUnit.getSymbol());
        
        return converted;
    }
    
    /**
     * Valida que una cantidad sea válida (no nula, no negativa, no cero)
     */
    public static void validateQuantity(BigDecimal quantity) {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                String.format("La cantidad debe ser mayor a 0. Recibido: %s", quantity)
            );
        }
    }
    
    /**
     * Valida que una unidad sea válida
     */
    public static void validateUnit(String unitSymbol) {
        try {
            UnitOfMeasure.fromSymbol(unitSymbol);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                String.format("Unidad de medida inválida: %s. Unidades válidas: %s",
                    unitSymbol, UnitOfMeasure.getValidSymbols())
            );
        }
    }
    
    /**
     * Valida que dos unidades sean compatibles
     */
    public static void validateUnitCompatibility(UnitOfMeasure unit1, UnitOfMeasure unit2) {
        if (!unit1.isCompatible(unit2)) {
            throw new IllegalArgumentException(
                String.format(
                    "Las unidades %s y %s no son compatibles. " +
                    "Unidades compatibles con %s: %s",
                    unit1.getSymbol(), unit2.getSymbol(),
                    unit1.getSymbol(), unit1.getCompatibleUnits())
            );
        }
    }
}
