package com.saswat.paygrid.common_lib.exception;

import lombok.Getter;

@Getter
public class InvalidStateTransitionException extends RuntimeException {

    private final String fromState;
    private final String toEvent;


    public InvalidStateTransitionException(String fromState, String event) {
        super("Invalid state transition from " + fromState + " to " + event);
        this.fromState = fromState;
        this.toEvent = event;
    }
}
