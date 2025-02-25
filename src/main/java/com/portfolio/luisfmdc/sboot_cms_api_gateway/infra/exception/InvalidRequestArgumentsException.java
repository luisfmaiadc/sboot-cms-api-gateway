package com.portfolio.luisfmdc.sboot_cms_api_gateway.infra.exception;

public class InvalidRequestArgumentsException extends IllegalArgumentException {
  public InvalidRequestArgumentsException(String message) {
    super(message);
  }
}
