public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // If there is no value - don't do comparison
    // If a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    try {
        // Check if the value is a valid URL
        new URI(String.valueOf(value)).toURL().toURI();
    } catch (URISyntaxException | MalformedURLException e) {
        addFieldError(fieldName, object);
    }
}