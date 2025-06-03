case NON_ABSENT:
    suppressNulls = true;
    if (actualType != null && actualType.isReferenceType()) {
        valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
    }
    break;
case ALWAYS:
default:
    if (actualType != null && actualType.isContainerType() && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
        valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
    }