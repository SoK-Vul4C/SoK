public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    if (value.getClass().equals(String.class) && Pattern.compile(getUrlRegex(), Pattern.CASE_INSENSITIVE).matcher(String.valueOf(value)).matches()) { // Fix: Replace || with &&
        // Do nothing, value is a valid URL
    } else {
        addFieldError(fieldName, object); // Fix: Move this statement inside the else block
    }
}