package com.topjava.graduation.web;

import com.topjava.graduation.util.ValidationUtil;
import com.topjava.graduation.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.topjava.graduation.util.exception.ErrorType.*;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static final Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);
    public static final String EXCEPTION_DUPLICATE_EMAIL = "A user with this email already exists";
    public static final String EXCEPTION_DUPLICATE_RESTAURANT_NAME = "A restaurant with that name already exists";
    public static final String EXCEPTION_DUPLICATE_MEAL = "This restaurant already has food with that name and date";
    private static final Map<String, String> CONSTRAINTS = Map.of(
            "users_unique_email_idx", EXCEPTION_DUPLICATE_EMAIL,
            "restaurant_unique_name_idx", EXCEPTION_DUPLICATE_RESTAURANT_NAME,
            "meal_restaurant_id_date_name_idx", EXCEPTION_DUPLICATE_MEAL);

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo notFoundError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(VotingRestrictionsException.class)
    public ErrorInfo votingRestriction(HttpServletRequest req, VotingRestrictionsException e) {
        return logAndGetErrorInfo(req, e, false, VOTING_RESTRICTIONS, e.getMessage());
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(BindException.class)
    public ErrorInfo bindValidationError(HttpServletRequest req, BindException e) {
        String[] details = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> "[" + fieldError.getField() + "] " + fieldError.getDefaultMessage())
                .toArray(String[]::new);
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, details);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class})
    public ErrorInfo validationError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            for (Map.Entry<String, String> entry : CONSTRAINTS.entrySet()) {
                if (lowerCaseMsg.contains(entry.getKey())) {
                    return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR, entry.getValue());
                }
            }
        }
        return logAndGetErrorInfo(req, e, true, DATA_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ErrorInfo wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetErrorInfo(req, e, false, WRONG_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo internalError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logStackTrace,
                                                ErrorType errorType, String... details) {
        ValidationUtil.logging(log, req, e, logStackTrace, errorType, details);
        return new ErrorInfo(req.getRequestURL(), errorType, details);
    }
}