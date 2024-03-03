package com.graduation.topjava.util.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Voting restriction")
public class VotingRestrictionsException extends RuntimeException {
    public VotingRestrictionsException(String message) {
        super(message);
    }
}
