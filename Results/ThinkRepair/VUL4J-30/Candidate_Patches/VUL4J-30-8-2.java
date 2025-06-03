public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().isEmpty()) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object, getValidatorContext());
        return;
    }

    String url = value.toString();
    if (!Pattern.compile(getUrlRegex(), Pattern.CASE_INSENSITIVE).matcher(url).matches()) {
        addFieldError(fieldName, object, getValidatorContext());
    }
}