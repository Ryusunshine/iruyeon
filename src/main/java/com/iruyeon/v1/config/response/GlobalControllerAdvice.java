package com.iruyeon.v1.config.response;

import com.iruyeon.v1.config.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static com.iruyeon.v1.config.response.ErrorCode.*;
import static com.iruyeon.v1.config.response.ErrorResponse.of;
import static org.springframework.core.NestedExceptionUtils.getMostSpecificCause;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ErrorResponse handleMehtodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.warn("[CommonException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return of(COMMON_BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = {ClientAbortException.class})
    public ErrorResponse handleSkippableException(Exception ex) {
        log.warn("[SkipException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return of(COMMON_INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.warn("[CommonException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return of(COMMON_METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = BaseException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BaseException ex) {
        log.warn("[CommonException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(of(ex.getErrorCode()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(Exception ex) {
        log.error("[ServerException] ", ex);
        return of(COMMON_INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.warn("[CommonException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return of(COMMON_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ErrorResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.warn("[CommonException] cause = {}, message = {}",
                getMostSpecificCause(ex),
                getMostSpecificCause(ex).getMessage());
        return ErrorResponse.of(PARAMETER_MISSING);
    }
}


