package com.altamar.shop.utils;

import com.altamar.shop.models.dto.Dto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Validator {

    private static final javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().usingContext().getValidator();

    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String NUMBER_REGEX = "(?<!\\w)(?:(?:(?:(?:\\+?3)?8\\W{0,5})?0\\W{0,5})?[34569]\\s?\\d[^\\w,;(\\+]{0,5})?\\d\\W{0,5}\\d\\W{0,5}\\d\\W{0,5}\\d\\W{0,5}\\d\\W{0,5}\\d\\W{0,5}\\d(?!(\\W?\\d))";
    private static final String IP_REGEX = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    /**
     * @param email any email address
     * @return true if email from argument is valid
     */
    public static boolean emailIsValid(String email) {
        final Pattern pattern = Pattern.compile(EMAIL_REGEX);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * @param ip any ip
     * @return true if ip from argument is valid
     */
//    public static boolean ipIsNotValid(String ip) {
//        return !ip.matches(IP_REGEX);
//    }

    /**
     * @param number any number
     * @return true if number from argument is valid
     */
    public static boolean numberIsValid(String number) {
        final Pattern pattern = Pattern.compile(NUMBER_REGEX);
        final Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    /**
     * @param <DTO> any DTO
     * @throws ValidationException if dto from argument is invalid
     */
    public static <DTO extends Dto> void dtoValidator(DTO dto) {
        final Set<ConstraintViolation<DTO>> validationResult = validator.validate(dto);
        if (validationResult.size() != 0) {
            final String errorMessage = validationResult.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * @param <T> any object
     * @throws ValidationException if object from argument is invalid
     */
    public static <T> void objectValidator(T t) {
        final Set<ConstraintViolation<T>> validationResult = validator.validate(t);
        if (validationResult.size() != 0) {
            final String errorMessage = validationResult.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n"));
            throw new ValidationException(errorMessage);
        }
    }

    /**
     * @param ids list of ids
     * @throws IllegalArgumentException if array of ids from argument not valid
     */
    public static void validateIds(Long... ids) {
        for (Long id : ids) {
            if (id < 0) {
                throw new IllegalArgumentException("Negative value of id");
            }
            log.info("[Validator] : Id is valid");
        }
    }
}
