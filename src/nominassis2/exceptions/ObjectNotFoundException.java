package nominassis2.exceptions;

import java.io.IOException;

public class ObjectNotFoundException extends IOException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String message) {
        super(message);
    }

}