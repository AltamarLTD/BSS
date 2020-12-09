package com.altamar.shop.controllers.handler;

import com.altamar.shop.models.dto.Response;
import com.altamar.shop.models.enums.ErrorCode;
import com.altamar.shop.models.exсeptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.io.FileNotFoundException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleValidationException(ValidationException validationException) {
        log.error(validationException.getLocalizedMessage());
        return Response.error(ErrorCode.VALIDATION_ERROR.getCode(), validationException.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleInvalidFormatException(InvalidFormatException invalidFormatException) {
        log.error(invalidFormatException.getLocalizedMessage());
        return Response.error(ErrorCode.INVALID_FORMAT_ERROR.getCode(), invalidFormatException.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<?> handleNotFoundException(NotFoundException notFoundException) {
        log.error(notFoundException.getLocalizedMessage());
        return Response.error(ErrorCode.NOT_FOUND.getCode(), notFoundException.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        log.error(illegalArgumentException.getLocalizedMessage());
        return Response.error(ErrorCode.ILLEGAL_ARGUMENT_ERROR.getCode(), illegalArgumentException.getMessage());
    }

    @ExceptionHandler(FileUploadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleFileUploadException(FileUploadException fileUploadException) {
        log.error(fileUploadException.getLocalizedMessage());
        return Response.error(ErrorCode.FILE_UPLOAD_ERROR.getCode(), fileUploadException.getMessage());
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<?> handleFileNotFoundException(FileNotFoundException fileNotFoundException) {
        log.error(fileNotFoundException.getLocalizedMessage());
        return Response.error(ErrorCode.NOT_FOUND.getCode(), fileNotFoundException.getMessage());
    }

    @ExceptionHandler(DeleteFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleDeleteFileException(DeleteFileException deleteFileException) {
        log.error(deleteFileException.getLocalizedMessage());
        return Response.error(ErrorCode.DELETE_FILE_ERROR.getCode(), deleteFileException.getMessage());
    }

    @ExceptionHandler(MailSenderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response<?> handleMailSenderException(MailSenderException mailSenderException) {
        log.error(mailSenderException.getLocalizedMessage());
        return Response.error(ErrorCode.MAIL_SEND_ERROR.getCode(), mailSenderException.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<?> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        log.error(entityNotFoundException.getLocalizedMessage());
        return Response.error(ErrorCode.NOT_FOUND.getCode(), entityNotFoundException.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<?> handleUsernameNotFoundException(UsernameNotFoundException usernameNotFoundException) {
        log.error(usernameNotFoundException.getLocalizedMessage());
        return Response.error(ErrorCode.NOT_FOUND.getCode(), usernameNotFoundException.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleBadCredentialException(BadCredentialsException badCredentialsException) {
        log.error(badCredentialsException.getLocalizedMessage());
        return Response.error(ErrorCode.BAD_CREDENTIALS_ERROR.getCode(), badCredentialsException.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleIllegalAccessException(IllegalAccessException illegalAccessException) {
        log.error(illegalAccessException.getLocalizedMessage());
        return Response.error(ErrorCode.ILLEGAL_ACCESS_ERROR.getCode(), illegalAccessException.getMessage());
    }

    @ExceptionHandler(TooMuchAuthAttemptsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleTooMuchAuthAttemptsException(TooMuchAuthAttemptsException tooMuchAuthAttemptsException) {
        log.error(tooMuchAuthAttemptsException.getLocalizedMessage());
        return Response.error(ErrorCode.TOO_MUCH_AUTH_ATTEMPTS_ERROR.getCode(), tooMuchAuthAttemptsException.getMessage());
    }

    @ExceptionHandler(LockedException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public Response<?> handleLockedException(LockedException lockedException) {
        log.error(lockedException.getLocalizedMessage());
        return Response.error(ErrorCode.LOCKED_ERROR.getCode(), lockedException.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public Response<?> handleDisabledException(DisabledException disabledException) {
        log.error(disabledException.getLocalizedMessage());
        return Response.error(ErrorCode.DISABLED_ERROR.getCode(), disabledException.getMessage());
    }

    @ExceptionHandler(AccountExpiredException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public Response<?> handleAccountExpiredException(AccountExpiredException accountExpiredException) {
        log.error(accountExpiredException.getLocalizedMessage());
        return Response.error(ErrorCode.ACCOUNT_EXPIRED_ERROR.getCode(), accountExpiredException.getMessage());
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    @ResponseStatus(HttpStatus.LOCKED)
    public Response<?> handleCredentialsExpiredException(CredentialsExpiredException credentialsExpiredException) {
        log.error(credentialsExpiredException.getLocalizedMessage());
        return Response.error(ErrorCode.CREDENTIALS_EXPIRED_ERROR.getCode(), credentialsExpiredException.getMessage());
    }

}
