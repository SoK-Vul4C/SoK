public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value == null || value.toString().isEmpty()) {
        return;
    }

    if (!(value instanceof String)) {
        addFieldError(fieldName, object);
        return;
    }

    String url = value.toString();
    if (!isValidUrl(url)) {
        addFieldError(fieldName, object);
    }
}

private boolean isValidUrl(String url) {
    try {
        new URL(url).toURI();
        return true;
    } catch (MalformedURLException | URISyntaxException e) {
        return false;
    }
}