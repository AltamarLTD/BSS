package com.altamar.shop.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    VALIDATION_ERROR(1),

    INVALID_FORMAT_ERROR(2),

    NOT_FOUND(3),

    ILLEGAL_ARGUMENT_ERROR(4),

    FILE_UPLOAD_ERROR(5),

    DELETE_FILE_ERROR(6),

    MAIL_SEND_ERROR(7),

    BAD_CREDENTIALS_ERROR(8),

    TOO_MUCH_AUTH_ATTEMPTS_ERROR(9),

    ILLEGAL_ACCESS_ERROR(10),

    LOCKED_ERROR(11),

    ACCOUNT_EXPIRED_ERROR(12),

    DISABLED_ERROR(13),

    CREDENTIALS_EXPIRED_ERROR(14);

    int code;

}
