package com.kindredgroup.unibetlivetest.types;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExceptionType {

    CUSTOMER_NOT_FOUND(HttpStatus.NOT_FOUND),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    SELECTION_NOT_FOUND(HttpStatus.NOT_FOUND),
    NO_LIVE(HttpStatus.NO_CONTENT),
    INSUFFICIENT_AMOUNT(HttpStatus.PRECONDITION_REQUIRED), // 600
    ODD_CHANGED(HttpStatus.PRECONDITION_REQUIRED), // 601
    SELECTION_CLOSED(HttpStatus.PRECONDITION_REQUIRED), // 602
    ONGOING_BET(HttpStatus.CONFLICT);

    @Getter
    final HttpStatus status;

    ExceptionType(HttpStatus status) {
        this.status = status;
    }

}
