package com.sartora.Sartora_backend.exception;


import lombok.Getter;
import lombok.Setter;

public class UserNotVerifiedException extends Exception {

    @Getter
    @Setter

    private boolean newEmailSent;
    public UserNotVerifiedException(boolean newEmailSent) {
        this.newEmailSent = newEmailSent;
    }

}
