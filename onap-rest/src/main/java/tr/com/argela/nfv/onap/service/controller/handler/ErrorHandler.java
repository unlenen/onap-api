package tr.com.argela.nfv.onap.service.controller.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import net.minidev.json.JSONObject;
import tr.com.argela.nfv.onap.api.exception.OnapException;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<String> handleIOException(IOException exception) {
        JSONObject error = new JSONObject();
        error.put("code", exception.getClass().getSimpleName());
        error.put("message", exception.getMessage());
        return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = OnapException.class)
    public ResponseEntity<String> handleOnapException(OnapException exception) {
        JSONObject error = new JSONObject();
        error.put("code", exception.getClass().getSimpleName());
        error.put("message", exception.getMessage());
        return new ResponseEntity<String>(error.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
