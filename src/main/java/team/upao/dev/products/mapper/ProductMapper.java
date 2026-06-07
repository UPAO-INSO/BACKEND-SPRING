package team.upao.dev.products.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import team.upao.dev.integrations.aws.service.S3Service;
import team.upao.dev.products.dto.ProductRequestDto;
import team.upao.dev.products.dto.ProductResponseDto;
import team.upao.dev.products.model.ProductModel;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Service s3Service;

    /**
     * Genera URL presignada de S3 para la imagen de un producto (válida por 1
     * hora).
     */
    private String buildImageUrl(ProductModel product) {
        try {
            String productKey = getProductImageKey(product);
            // Generar URL presignada válida por 1 hora
            return s3Service.generatePresignedUrl(bucketName, productKey, Duration.ofHours(1));
        } catch (Exception e) {
            // En caso de error, retornar URL directa como fallback
            String slug = slugify(product.getName());
            return String.format("https://%s.s3.%s.amazonaws.com/products/%s.webp",
                    bucketName, region, slug);
        }
    }

    /** Obtiene la clave (key) del objeto en S3 para una imagen de producto. */
    private String getProductImageKey(ProductModel product) {
        // Si ya tiene URL guardada, extraer la clave de ella
        if (product.getImageUrl() != null && !product.getImageUrl().isBlank()) {
            // Asumir formato: products/producto-name.webp
            if (product.getImageUrl().contains("products/")) {
                return product.getImageUrl().substring(product.getImageUrl().indexOf("products/"));
            }
            return product.getImageUrl();
        }
        // Construir desde el nombre del producto
        String slug = slugify(product.getName());
        return String.format("products/%s.webp", slug);
    }

    private String slugify(String name) {
        if (name == null)
            return "placeholder";
        return name.toLowerCase()
                .trim()
                .replaceAll("[áàäâ]", "a")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöô]", "o")
                .replaceAll("[úùüû]", "u")
                .replaceAll("[^a-z0-9ñ]+", "-")
                .replaceAll("^-+|-+$", "");
    }

    public ProductModel toModel(ProductRequestDto dto) {
        return ProductModel.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .productType(null)
                .active(dto.getActive() != null ? dto.getActive() : true)
                .available(dto.getAvailable() != null ? dto.getAvailable() : true)
                .build();
    }

    public List<ProductModel> toModel(List<ProductRequestDto> dtos) {
        return dtos.stream().map(this::toModel).toList();
    }

    public ProductResponseDto toDto(ProductModel product) {
        // Stock directo desde el FK inventory_id (solo para bebidas/descartables)
        java.math.BigDecimal stock = null;
        if (product.getInventoryItem() != null) {
            stock = product.getInventoryItem().getQuantity();
        }

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .productTypeId(product.getProductType().getId())
                .productTypeName(product.getProductType().getName())
                .active(product.getActive())
                .available(product.getAvailable())
                .imageUrl(buildImageUrl(product))
                .stock(stock)
                .build();
    }

    public List<ProductResponseDto> toDto(List<ProductModel> products) {
        return products.stream().map(this::toDto).toList();
    }
}
