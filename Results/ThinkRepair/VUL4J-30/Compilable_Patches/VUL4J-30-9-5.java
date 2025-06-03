public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    if (value instanceof String) {
        String strValue = (String) value;

        if (!strValue.isEmpty()) {
            if (!Pattern.compile(getUrlRegex(), Pattern.CASE_INSENSITIVE).matcher(strValue).matches()) {
                addFieldError(fieldName, object);
            }
        }
    }
}