package com.defitech.GestUni.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false)
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorResponse);
    }
    public static class IncidentAlreadyTakenException extends RuntimeException {
        public IncidentAlreadyTakenException(String message) {
            super(message);
        }
    }
    public static class IncidentNotTakenException extends RuntimeException {
        public IncidentNotTakenException(String message) {
            super(message);
        }
    }
    public static class IncidentNotFoundException extends RuntimeException {
        public IncidentNotFoundException(String message) {
            super(message);
        }
    }
    public static class IncidentAlreadyResolvedException extends RuntimeException {
        public IncidentAlreadyResolvedException(String message) {
            super(message);
        }
    }
    public static class NotificationException extends RuntimeException {
        public NotificationException(String message) {
            super(message);
        }
    }
    public class AvailabilityException extends RuntimeException {
        public static class PastOrFutureError extends RuntimeException {
            public PastOrFutureError(String message) {
                super(message);
            }
        }

        public static class OverlapError extends RuntimeException {
            public OverlapError(String message) {
                super(message);
            }
        }

        public static class InvalidTimeRangeError extends RuntimeException {
            public InvalidTimeRangeError(String message) {
                super(message);
            }
        }
    }
    // You can add more handlers here for other exceptions

    // Inner class for error response
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;

        }

        // Getters and setters
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            return "ErrorResponse{" +
                    "timestamp=" + timestamp +
                    ", status=" + status +
                    ", error='" + error + '\'' +
                    ", message='" + message + '\'' +
                    ", path='" + path + '\'' +
                    '}';
        }
    }

    // Define custom exceptions within the handler
    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class PseudoAlreadyExistsException extends RuntimeException {
        public PseudoAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {
            super(message);
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // Gestion d'autres exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
    }

    // Uncomment and add more exception handlers as needed
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "User Already Exists", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(PseudoAlreadyExistsException.class)
//    public ResponseEntity<ErrorResponse> handlePseudoAlreadyExistsException(PseudoAlreadyExistsException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(), "Pseudo Already Exists", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(InvalidPasswordException.class)
//    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(InvalidPasswordException ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Invalid Password", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage(), request.getDescription(false));
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(errorResponse);
//    }



//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(IOException.class)
//    public ResponseEntity<String> handleIOException(IOException e) {
//        return new ResponseEntity<>("Error uploading file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception e) {
//        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
