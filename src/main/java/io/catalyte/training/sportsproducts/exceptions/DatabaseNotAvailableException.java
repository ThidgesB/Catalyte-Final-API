package io.catalyte.training.sportsproducts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A custom exception indicating the database is not available.
 */
@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class DatabaseNotAvailableException extends RuntimeException {
  public DatabaseNotAvailableException(String message) {
    super(message);
  }
}
