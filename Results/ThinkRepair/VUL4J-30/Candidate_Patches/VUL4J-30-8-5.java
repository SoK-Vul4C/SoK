import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value == null || value.toString().isEmpty()) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object, getValidatorContext());
        return;
    }

    String urlString = value.toString();
    if (!isValidUrl(urlString)) {
        addFieldError(fieldName, object, getValidatorContext());
    }
}

private boolean isValidUrl(String urlString) {
    try {
        URL url = new URL(urlString);
        url.toURI();
        return true;
    } catch (MalformedURLException | URISyntaxException e) {
        return false;
    }
}