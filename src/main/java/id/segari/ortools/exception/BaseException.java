package id.segari.ortools.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
public class BaseException extends RuntimeException {
    private String message;
    private String errorCode;
    private HttpStatus httpStatus;
}
