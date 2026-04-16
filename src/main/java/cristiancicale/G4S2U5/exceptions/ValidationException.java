package cristiancicale.G4S2U5.exceptions;

import java.util.List;

public class ValidationException extends RuntimeException {
    private List<String> errors;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(List<String> errors) {
        super("errori di validazione");
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
