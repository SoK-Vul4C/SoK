import java.util.regex.Pattern;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // If the value is null or empty, no further validation is needed
    if (value == null || value.toString().isEmpty()) {
        return;
    }

    // Perform URL validation only on String instances
    if (value instanceof String) {
        String url = (String) value;
        String urlRegex = getUrlRegex();

        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(url).matches()) {
            addFieldError(fieldName, object);
        }
    }
}