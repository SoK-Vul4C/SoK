import java.net.MalformedURLException;
import java.net.URL;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // If there is no value or it is not a string - don't do comparison
    // If a value is required, a required validator should be added to the field
    if (value == null || !(value instanceof String) || ((String) value).isEmpty()) {
        return;
    }

    String url = (String) value;

    try {
        new URL(url).toURI();
    } catch (MalformedURLException | IllegalArgumentException | ExceptionInInitializerError e) {
        addFieldError(fieldName, object);
    }
}