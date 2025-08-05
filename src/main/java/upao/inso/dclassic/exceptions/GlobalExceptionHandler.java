package upao.inso.dclassic.exceptions;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handlerArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handlerRuntimeException(RuntimeException ex, WebRequest request) {
        // Recordar cambiar el mensaje de error para no ser espécifico
        // return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado en el servidor.", request.getDescription(false)));
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDatabaseException(DataAccessException ex, WebRequest request) {
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error de base de datos: " + ex.getMessage(), request.getDescription(false)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, List<String>> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
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
        String specificMsg = buildHttpMessageNotReadableMessage(ex);
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, specificMsg, request.getDescription(false)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex, WebRequest request) {
        // Recordar cambiar el mensaje de error para no ser específico
        // return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno en el servidor.", request.getDescription(false)));
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno: " + ex.getMessage(), request.getDescription(false)));
    }

    private String buildHttpMessageNotReadableMessage(HttpMessageNotReadableException ex) {
        String specificMsg = "Cuerpo de la solicitud malformado";
        Throwable cause = ex.getCause();

        if (cause != null && cause.getMessage() != null) {
            String msg = cause.getMessage();

            if (msg.contains("not one of the values accepted for Enum class")) {
                int start = msg.indexOf("[");
                int end = msg.indexOf("]");
                String validValues = (start != -1 && end != -1) ? msg.substring(start + 1, end) : "";
                specificMsg = "Valor inválido para un campo enum. Debe ser uno de: " + validValues;
            } else if (msg.contains("Cannot deserialize value of type")) {
                int typeStart = msg.indexOf("`");
                int typeEnd = msg.indexOf("`", typeStart + 1);
                String type = (typeStart != -1 && typeEnd != -1) ? msg.substring(typeStart + 1, typeEnd) : "tipo desconocido";
                specificMsg = "Tipo de dato inválido. Se esperaba: " + type;
            } else if (msg.contains("Unexpected character") || msg.contains("Unrecognized token")) {
                specificMsg = "JSON mal formado o contiene caracteres no válidos.";
            } else {
                specificMsg = "Error al deserializar el cuerpo de la solicitud.";
            }
        }
        return specificMsg;
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @Data
    @AllArgsConstructor @NoArgsConstructor
    public static class ApiError {
        private LocalDateTime timestamp = LocalDateTime.now();
        private HttpStatus status;
        private String message;
        private String details;
        private Map<String, List<String>> errors;

        public ApiError(HttpStatus status, String message, String details) {
            this.status = status;
            this.message = message;
            this.details = details;
        }
    }
}
