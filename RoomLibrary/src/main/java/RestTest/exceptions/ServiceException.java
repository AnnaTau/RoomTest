package RestTest.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Tau on 05.09.2015.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ServiceException extends Exception {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }
}
