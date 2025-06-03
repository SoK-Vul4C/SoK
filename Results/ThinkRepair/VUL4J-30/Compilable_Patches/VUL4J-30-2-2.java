public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().isEmpty()) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object); // Invalid type, add field error
        return;
    }

    String strValue = (String) value;
    if (!isValidURL(strValue)) {
        addFieldError(fieldName, object); // Invalid URL, add field error
    }
}

private boolean isValidURL(String url) {
    // URL regex pattern for validation
    String regex = "^((https?|ftp|file)://|www\\.)[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$";
    return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(url).matches();
}