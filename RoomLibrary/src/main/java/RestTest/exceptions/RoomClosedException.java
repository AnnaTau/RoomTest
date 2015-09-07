package RestTest.exceptions;

/**
 * Created by Tau on 05.09.2015.
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RoomClosedException extends Exception {

    public RoomClosedException() {
    }

    public RoomClosedException(String cause) {
        super(cause);
    }
}
