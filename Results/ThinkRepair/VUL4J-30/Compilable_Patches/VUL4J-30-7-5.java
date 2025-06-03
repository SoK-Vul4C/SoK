import java.net.URL;
import java.net.URISyntaxException;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value == null || !(value instanceof String) || ((String) value).isEmpty()) {
        return;
    }

    String url = (String) value;

    try {
        new URL(url).toURI();
    } catch (URISyntaxException | Exception e) {
        addFieldError(fieldName, object);
    }
}