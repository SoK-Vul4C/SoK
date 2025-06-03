import java.net.MalformedURLException;
import java.net.URL;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    if (value instanceof String) {
        try {
            new URL(value.toString());
        } catch (MalformedURLException e) {
            addFieldError(fieldName, object);
        }
    }
}