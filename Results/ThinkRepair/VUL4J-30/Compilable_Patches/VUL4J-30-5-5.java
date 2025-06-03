import java.util.regex.Pattern;
import java.util.regex.Matcher;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value == null || !(value instanceof String) || ((String) value).isEmpty()) {
        return; // No validation needed if value is null or not a string or empty
    }

    String urlRegex = getUrlRegex();
    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher((String) value);

    if (!matcher.matches()) {
        addFieldError(fieldName, object);
    }
}