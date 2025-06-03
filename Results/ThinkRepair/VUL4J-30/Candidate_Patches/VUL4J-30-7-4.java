import java.net.MalformedURLException;
import java.net.URL;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value == null || !(value instanceof String) || ((String) value).isEmpty()) {
        return;
    }

    String url = (String) value;

    try {
        new URL(url).toURI();
    } catch (MalformedURLException | IllegalArgumentException | ExceptionInInitializerError | SecurityException e) {
        addFieldError(fieldName, object);
    }
}