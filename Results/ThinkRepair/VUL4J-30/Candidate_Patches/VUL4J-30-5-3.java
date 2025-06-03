public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object); // Value is not a String
        return;
    }

    String urlRegex = getUrlRegex();
    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher((String) value);

    if (!matcher.matches()) {
        addFieldError(fieldName, object); // Value does not match URL regex
    }
}