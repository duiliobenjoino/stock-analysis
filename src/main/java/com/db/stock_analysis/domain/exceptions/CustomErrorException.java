package com.db.stock_analysis.domain.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class CustomErrorException extends RuntimeException  {

    private final String description;
    private final int status;

    private CustomErrorException(final String message, final int status, final String description) {
        super(message);
        this.description = description;
        this.status = status;
    }

    public static CustomErrorException of(final String message, final int status, final Throwable throwable) {
        log.error(message, throwable);
        return new CustomErrorException(message, status, throwable.getMessage());
    }

    public static CustomErrorException of(final String message, final int status) {
        log.error(message, message);
        return new CustomErrorException(message, status, message);
    }
}
