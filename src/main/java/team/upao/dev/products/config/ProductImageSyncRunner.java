package team.upao.dev.products.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import team.upao.dev.products.service.ProductService;

/**
 * Al arrancar la aplicación, sincroniza las imageUrl de los productos
 * con los archivos que existen en el bucket S3 bajo el prefijo "products/".
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ProductImageSyncRunner {

    private final ProductService productService;

    @EventListener(ApplicationReadyEvent.class)
    public void syncOnStartup() {
        try {
            log.info("Ejecutando sincronización automática de imágenes con S3...");
            int updated = productService.syncImagesFromS3();
            log.info("Sincronización de imágenes completada: {} producto(s) actualizados.", updated);
        } catch (Exception e) {
            log.warn("No se pudo sincronizar imágenes con S3 al arrancar: {}", e.getMessage());
        }
    }
}
