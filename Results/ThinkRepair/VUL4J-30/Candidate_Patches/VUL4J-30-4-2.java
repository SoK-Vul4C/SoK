public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().isEmpty()) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object);
        return;
    }

    String url = (String) value;
    if (!isUrlValid(url)) {
        addFieldError(fieldName, object);
    }
}

private boolean isUrlValid(String url) {
    try {
        new URL(url).toURI();
        return true;
    } catch (MalformedURLException | URISyntaxException e) {
        return false;
    }
}