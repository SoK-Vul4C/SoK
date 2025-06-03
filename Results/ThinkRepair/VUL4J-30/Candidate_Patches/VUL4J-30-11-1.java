public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    if (value.getClass().equals(String.class) && Pattern.compile(getUrlRegex(), Pattern.CASE_INSENSITIVE).matcher(String.valueOf(value)).matches()) {
        // value is a String and matches the URL regex pattern
        // No need to add field error
    } else {
        addFieldError(fieldName, object); // Fix: Moved this line inside else block
    }
}