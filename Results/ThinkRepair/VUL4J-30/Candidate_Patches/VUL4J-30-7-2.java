import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // If there is no value - don't do comparison
    // If a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    // Check if the value is a valid URL
    if (!(value instanceof String)) {
        addFieldError(fieldName, object);
        return;
    }

    String url = (String) value;

    try {
        new URL(url).toURI();
    } catch (MalformedURLException e) {
        addFieldError(fieldName, object);
    }
}