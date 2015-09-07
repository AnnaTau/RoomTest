package RestTest.exceptions;

/**
 * Created by Tau on 05.09.2015.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ObjectException extends Exception {
    public ObjectException() {
    }

    public ObjectException(String message) {
        super(message);
    }
}
