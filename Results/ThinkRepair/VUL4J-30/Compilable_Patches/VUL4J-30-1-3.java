import java.net.URI;

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
            // Try to parse the value as a URI to validate it as a URL
            new URI(value.toString());
        } catch (Exception e) {
            addFieldError(fieldName, object);
        }
    }
}