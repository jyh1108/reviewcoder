package io.reviewcoder.global.error;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String path, Map<String,String> fields) {
        ApiError body = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .fieldErrors(fields == null ? Map.of() : fields)
                .path(path)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    // 400: @Valid 바디 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleBodyValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String,String> fieldMap = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(fe -> fieldMap.put(fe.getField(), fe.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다.", req.getRequestURI(), fieldMap);
    }

    // 400: @RequestParam/@PathVariable 검증 실패
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String,String> fieldMap = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(v ->
                fieldMap.put(v.getPropertyPath().toString(), v.getMessage()));
        return build(HttpStatus.BAD_REQUEST, "요청 값 검증에 실패했습니다.", req.getRequestURI(), fieldMap);
    }

    // 400: JSON 파싱, 타입 불일치, 필수 파라미터 누락 등
    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "요청을 해석할 수 없습니다.", req.getRequestURI(), null);
    }

    // 400: 명시적 잘못된 인자
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST,
                ex.getMessage() == null ? "잘못된 요청입니다." : ex.getMessage(),
                req.getRequestURI(), null);
    }

    // 404: 조회 실패 계열
    @ExceptionHandler({ NoSuchElementException.class, EmptyResultDataAccessException.class })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND,
                ex.getMessage() == null ? "리소스를 찾을 수 없습니다." : ex.getMessage(),
                req.getRequestURI(), null);
    }

    // 서비스/컨트롤러에서 명시적으로 던지는 표준 예외
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
        return build(HttpStatus.valueOf(ex.getStatusCode().value()),
                ex.getReason() == null ? "요청을 처리할 수 없습니다." : ex.getReason(),
                req.getRequestURI(), null);
    }

    // 405: 메서드 미지원
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 메서드입니다.", req.getRequestURI(), null);
    }

    // 500: 그 외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.", req.getRequestURI(), null);
    }
}
