import org.apache.commons.validator.routines.UrlValidator;

public void validate(Object object) throws ValidationException {
    String fieldName = getFieldName();
    Object value = this.getFieldValue(fieldName, object);

    // if there is no value - don't do comparison
    // if a value is required, a required validator should be added to the field
    if (value == null || value.toString().length() == 0) {
        return;
    }

    UrlValidator urlValidator = new UrlValidator();
    if (!urlValidator.isValid(value.toString())) {
        addFieldError(fieldName, object);
    }
}