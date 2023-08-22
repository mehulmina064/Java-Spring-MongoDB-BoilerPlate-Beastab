package com.beastab.dataservice.controllerAdvisor;

import com.beastab.dataservice.common.exceptions.UnauthorizedException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.webjars.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {


    @Value("${environment.type}")
    private String environmentType;

    // Custom error response object
    @Data
    public static class ErrorResponse {
        private long timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(long timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        // Retrieve the API path
        String apiPath = request.getRequestURI();
        int statusCode = response.getStatus();
        String error = "Internal Server Error";

        boolean runTimeException = ex instanceof RuntimeException;
        boolean notFoundException = ex instanceof NotFoundException;
        boolean tokenExpiryException = ex instanceof UnauthorizedException;

        statusCode = notFoundException ? 404 : statusCode;
        error = notFoundException ? ex.getMessage() : error;
        error = tokenExpiryException ? ex.getMessage() : error;
        statusCode = tokenExpiryException ? 401 : statusCode;
        // Create the error response object
        ErrorResponse errorResponse = new ErrorResponse(System.currentTimeMillis(), statusCode == 200 ? 500 : statusCode, error, ex.getMessage(), apiPath);

        // Return the error response with appropriate status code
        return new ResponseEntity<>(errorResponse,  HttpStatus.valueOf(statusCode == 200 ? 500 : statusCode));
    }

    private boolean isProdProfileActive() {
        return StringUtils.equalsIgnoreCase(environmentType, "PROD");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String errorMessage = getValidationErrorMessage(bindingResult);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<ObjectError> errors = bindingResult.getAllErrors();
        for (ObjectError error : errors) {
            errorMessage.append(error.getDefaultMessage()).append("; ");
        }
        return errorMessage.toString();
    }

    private String getStackTraceAsString(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        ex.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
