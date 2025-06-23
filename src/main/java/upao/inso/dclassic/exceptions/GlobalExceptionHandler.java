package upao.inso.dclassic.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handlerArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handlerRuntimeException(RuntimeException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_GATEWAY, ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDatabaseException(DataAccessException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error de base de datos: " + ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error de validación", request.getDescription(false));
        apiError.setErrors(errors);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String message = String.format("El parámetro '%s' debe ser de tipo '%s'", ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, message, request.getDescription(false)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, "Cuerpo de la solicitud malformado", request.getDescription(false)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno: " + ex.getMessage(), request.getDescription(false)));
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class ApiError {
        private LocalDateTime timestamp = LocalDateTime.now();
        private HttpStatus status;
        private String message;
        private String details;
        private Map<String, String> errors;

        public ApiError(HttpStatus status, String message, String details) {
            this.status = status;
            this.message = message;
            this.details = details;
        }
    }
}
