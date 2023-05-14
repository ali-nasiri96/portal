package com.example.portal.configuration;

import com.example.portal.model.dto.response.BasicResponse;
import com.example.portal.model.exception.UnauthorizedAccess;
import com.example.portal.model.exception.WebException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccess.class)
    ResponseEntity<BasicResponse> handleUnauthorizedAccessException(UnauthorizedAccess ex) {
        BasicResponse basicResponse = new BasicResponse(BasicResponse.Status.FAILURE, ex.getMessage());
        return new ResponseEntity<>(basicResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(WebException.class)
    ResponseEntity<BasicResponse> handleWebException(WebException ex) {
        BasicResponse basicResponse = new BasicResponse(BasicResponse.Status.FAILURE, ex.getMessage());
        return new ResponseEntity<>(basicResponse, BAD_REQUEST);
    }

    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();

        String errorMessage = fieldErrors.get(0).getDefaultMessage();

        return ResponseEntity.badRequest().body(errorMessage);
    }



}
