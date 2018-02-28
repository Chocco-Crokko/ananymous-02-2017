package application.utils.exceptions;


import application.utils.responses.MessageResponse;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class ExceptionsHandler {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionsHandler.class.getSimpleName());

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity alreadyExist() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new MessageResponse("User already exist"));
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public ResponseEntity databaseError(@NotNull Exception e) {
        LOGGER.error("Some problems with database", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("There was an error with the database, try again later"));
    }

    @ExceptionHandler(GeneratedKeyException.class)
    public ResponseEntity getKeyError(@NotNull GeneratedKeyException e) {
        LOGGER.error("Database changes method of getting generated key", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageResponse("User was registered, but there was a problem in getting id"));
    }
}

