package team.upao.dev.inventory.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Enum que define todas las unidades de medida permitidas en el sistema
 * con capacidad de conversión automática entre unidades compatibles
 */
public enum UnitOfMeasure {
    
    // ========== UNIDADES DE MASA ==========
    MG("mg", "MASS", BigDecimal.ONE),                                    // Miligramos (base)
    G("g", "MASS", new BigDecimal("1000")),                              // Gramos
    KG("kg", "MASS", new BigDecimal("1000000")),                         // Kilogramos
    
    // ========== UNIDADES DE VOLUMEN ==========
    ML("ml", "VOLUME", BigDecimal.ONE),                                  // Mililitros (base)
    L("L", "VOLUME", new BigDecimal("1000")),                            // Litros
    
    // ========== UNIDADES DE CANTIDAD ==========
    UNIDAD("unidad", "QUANTITY", BigDecimal.ONE);                          // Unidades individuales
    
    private final String symbol;           // Símbolo de la unidad (g, kg, ml, L, etc.)
    private final String category;         // Categoría: MASS, VOLUME, QUANTITY
    private final BigDecimal conversionFactor;  // Factor de conversión a la unidad base
    
    UnitOfMeasure(String symbol, String category, BigDecimal conversionFactor) {
        this.symbol = symbol;
        this.category = category;
        this.conversionFactor = conversionFactor;
    }
    
    /**
     * Obtiene el símbolo de la unidad
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Obtiene la categoría de la unidad (MASS, VOLUME, QUANTITY)
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Obtiene el factor de conversión a la unidad base
     */
    public BigDecimal getConversionFactor() {
        return conversionFactor;
    }
    
    /**
     * Verifica si dos unidades son compatibles (pueden convertirse entre sí)
     * @param other Otra unidad de medida
     * @return true si pueden convertirse, false en caso contrario
     */
    public boolean isCompatible(UnitOfMeasure other) {
        if (other == null) return false;
        return this.category.equals(other.category);
    }
    
    /**
     * Convierte una cantidad de esta unidad a otra unidad compatible
     * @param quantity Cantidad a convertir
     * @param targetUnit Unidad destino
     * @return Cantidad convertida
     * @throws IllegalArgumentException si las unidades no son compatibles
     */
    public BigDecimal convertTo(BigDecimal quantity, UnitOfMeasure targetUnit) {
        if (!this.isCompatible(targetUnit)) {
            throw new IllegalArgumentException(
                String.format("No se puede convertir de %s (%s) a %s (%s). Categorías incompatibles.",
                    this.symbol, this.category,
                    targetUnit.symbol, targetUnit.category)
            );
        }
        
        if (this.equals(targetUnit)) {
            return quantity;
        }
        
        // Convertir a unidad base, luego a unidad destino
        BigDecimal inBaseUnit = quantity.multiply(this.conversionFactor);
        return inBaseUnit.divide(targetUnit.conversionFactor, 10, java.math.RoundingMode.HALF_UP);
    }
    
    /**
     * Obtiene todas las unidades compatibles con esta unidad
     */
    public Set<UnitOfMeasure> getCompatibleUnits() {
        Set<UnitOfMeasure> compatible = new HashSet<>();
        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
            if (this.isCompatible(unit)) {
                compatible.add(unit);
            }
        }
        return compatible;
    }
    
    /**
     * Busca una unidad por su símbolo
     */
    public static UnitOfMeasure fromSymbol(String symbol) {
        for (UnitOfMeasure unit : UnitOfMeasure.values()) {
            if (unit.symbol.equalsIgnoreCase(symbol)) {
                return unit;
            }
        }
        throw new IllegalArgumentException(
            String.format("Unidad de medida no reconocida: %s. Unidades válidas: %s",
                symbol, getValidSymbols())
        );
    }
    
    /**
     * Retorna todas las unidades válidas como string
     */
    public static String getValidSymbols() {
        return Arrays.toString(Arrays.stream(UnitOfMeasure.values())
            .map(u -> u.symbol)
            .toArray());
    }
}