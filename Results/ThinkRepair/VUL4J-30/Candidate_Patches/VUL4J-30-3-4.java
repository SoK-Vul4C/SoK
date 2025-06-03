public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value or the value is not a String - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || !(value instanceof String) || ((String) value).isEmpty()) {
        return;
    }

    String urlRegex = getUrlRegex();
    String strValue = ((String) value).toLowerCase(); // Convert to lowercase for case-insensitive comparison
    
    // Prepare the pattern for case-insensitive matching
    Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);

    if (!pattern.matcher(strValue).find()) {
        addFieldError(fieldName, object);
    }
}