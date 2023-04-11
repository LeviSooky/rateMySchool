package com.ratemyschool.main.configuration;

import com.ratemyschool.main.exception.ErrorResponse;
import com.ratemyschool.main.exception.RmsRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { RmsRuntimeException.class })
    protected ResponseEntity<Object> handleConflict(RmsRuntimeException ex, WebRequest request) {
        ErrorResponse message = new ErrorResponse(
                ((ServletWebRequest)request).getRequest().getRequestURI(),
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        log.debug(message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(ex, message, headers, HttpStatus.BAD_REQUEST, request);
    }
}
