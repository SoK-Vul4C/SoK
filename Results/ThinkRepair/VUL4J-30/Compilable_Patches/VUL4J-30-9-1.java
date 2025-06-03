public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    if (!(value instanceof String) || !Pattern.compile(getUrlRegex(), Pattern.CASE_INSENSITIVE).matcher(String.valueOf(value)).matches()) { // Fix: Check if value is not a String OR does not match URL regex
        addFieldError(fieldName, object); // Fix: Add field error only if validation fails
    }
}