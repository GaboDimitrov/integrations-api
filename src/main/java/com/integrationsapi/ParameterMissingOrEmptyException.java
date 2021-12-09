package com.integrationsapi;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ParameterMissingOrEmptyException extends RuntimeException {
}
