package com.tiss.vitagergateway.exception;

import com.tiss.vitagergateway.component.MessageComponent;
import com.tiss.vitagergateway.dto.ErrorDto;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SignatureException;
import java.sql.SQLException;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String LOAD_BALANCER_ERROR = "Load balancer does not have";
    private static final String SSO_SERVICE = "SsoService";
    private static final String ERROR = "Error";
    private final MessageComponent messageByLocaleComponent;

    @Autowired
    public GlobalExceptionHandler(MessageComponent messageByLocaleComponent) {
        this.messageByLocaleComponent = messageByLocaleComponent;
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDto> httpMessageNotReadableException(Exception exception) {
        log.info(exception.getMessage(), exception);
        String message = "Invalid value";
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({SQLException.class})
    public ResponseEntity<ErrorDto> sqlException(SQLException exception) {
        log.info(exception.getMessage(), exception);
        String message = messageByLocaleComponent.getMessage("internal.server.server.err.msg");
        return new ResponseEntity<>(createResponse(INTERNAL_SERVER_ERROR, message), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorDto> sizaException(Exception exception) {
        log.info(exception.getMessage(), exception);
        String message = "file size must be lower than 23MB";
        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, message), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDto> methodArgumentNotValidException(Exception exception) {
        log.info(exception.getMessage(), exception);
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setCode(BAD_REQUEST.value());
        String message = "";
        errorDTO.setMessage(((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors().stream().map(fieldError -> {
            String errorMessage = fieldError.getField() + fieldError.getDefaultMessage();
            log.info(errorMessage);
            return errorMessage;
        }).collect(Collectors.joining(" \n ")));
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<ErrorDto> noHandlerFoundException(NoHandlerFoundException exception) {
//        log.info(exception.getMessage(), exception);
//        return new ResponseEntity<>(createResponse(HttpStatus.BAD_REQUEST, exception.getMessage()), HttpStatus.BAD_REQUEST);
//    }


//    @ExceptionHandler(DataIntegrityViolationException.class)
////    public ResponseEntity<ErrorDto> noHandlerFoundException(Exception exception) {
////        log.info(exception.getMessage(), exception);
////        ErrorDto errorDTO = new ErrorDto();
////        errorDTO.setCode(BAD_REQUEST.value());
////        errorDTO.setMessage("subCategory is used on Ticket");
////        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
////    }

    private ErrorDto createResponse(HttpStatus httpStatus, String message) {
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setCode(httpStatus.value());
        errorDTO.setType(ERROR);
        errorDTO.setMessage(message);
        log.info(errorDTO.toString());
        return errorDTO;
    }


    @ExceptionHandler({CustomException.class, CustomException.AuthorizationFailed.class, CustomException.BadRequest.class, CustomException.Edit.class, CustomException.FaultIsComplete.class, CustomException.Forbidden.class, CustomException.InputParamNotValid.class, CustomException.ItemNotFound.class, CustomException.NotFound.class, CustomException.Post.class, CustomException.ServerError.class, CustomException.ValidationFailure.class, CompletionException.class,})
    public ResponseEntity<ErrorDto> exceptionHandler(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        String message = "";
        Throwable exception = e.getCause() != null ? e.getCause() : e;

        ResponseStatus httpStatusAnnotation = getExceptionErrorCode(exception);
        HttpStatus httpStatus = httpStatusAnnotation != null ? httpStatusAnnotation.value() : HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setCode(httpStatus.value());

        if (exception instanceof BaseException) {
            message = exception.getMessage();
            if (message != null) {
                if (message.contains("#")) {
                    log.info(exception.getMessage(), exception);
                    message = message.split("#")[0] + messageByLocaleComponent.getMessage("out.of.service.err.msg");
                } else if (message.contains(LOAD_BALANCER_ERROR)) {
                    log.info(exception.getMessage(), exception);
                    message = message.split(":")[2] + messageByLocaleComponent.getMessage("out.of.service.err.msg");
                }

                if (message.toUpperCase().contains(SSO_SERVICE.toUpperCase())) {
                    message = messageByLocaleComponent.getMessage("token.expired.err.msg");
                    errorDTO.setCode(HttpStatus.UNAUTHORIZED.value());
                    httpStatus = HttpStatus.UNAUTHORIZED;
                }
            }
        } else {
            log.info(exception.getMessage(), exception);
            message = messageByLocaleComponent.getMessage("internal.server.server.err.msg");
        }

        errorDTO.setType(ERROR);
        errorDTO.setMessage(message);
        log.info(errorDTO.toString());
        return new ResponseEntity<>(errorDTO, httpStatus);
    }

    private ResponseStatus getExceptionErrorCode(Throwable exception) {
        return AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
    }

    @Order
    @ExceptionHandler(Exception.class)
    public final ProblemDetail  handleRuntimeExceptions(Exception exception) { //ResponseEntity<Object>
        ProblemDetail errorDetail = null;

        // TODO send this stack trace to an observability tool
        exception.printStackTrace();

        if (exception instanceof BadCredentialsException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), exception.getMessage());
            errorDetail.setProperty("description", "The username or password is incorrect");

            return errorDetail;
        }

        if (exception instanceof AccountStatusException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The account is locked");
        }

        if (exception instanceof AccessDeniedException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "You are not authorized to access this resource");
        }

        if (exception instanceof SignatureException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT signature is invalid");
        }

        if (exception instanceof ExpiredJwtException) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), exception.getMessage());
            errorDetail.setProperty("description", "The JWT token has expired");
        }

        if (errorDetail == null) {
            errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), exception.getMessage());
            errorDetail.setProperty("description", "Unknown internal server error.");
        }

        return errorDetail;
        /*
        ErrorDto errorDTO = new ErrorDto();
        errorDTO.setCode(INTERNAL_SERVER_ERROR.value());
        errorDTO.setMessage(messageByLocaleComponent.getMessage("internal.server.server.err.msg"));
        log.error(exception.getMessage(), exception);
        log.info(errorDTO.toString());
        return new ResponseEntity<>(errorDTO, INTERNAL_SERVER_ERROR);
        */

    }
}