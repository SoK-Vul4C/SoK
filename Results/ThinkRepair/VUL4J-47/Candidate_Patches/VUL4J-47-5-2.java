protected BeanPropertyWriter buildWriter(SerializerProvider prov,
        BeanPropertyDefinition propDef, JavaType declaredType, JsonSerializer<?> ser,
        TypeSerializer typeSer, TypeSerializer contentTypeSer,
        AnnotatedMember am, boolean defaultUseStaticTyping)
    throws JsonMappingException
{
    // do we have annotation that forces type to use (to declared type or its super type)?
    JavaType serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);

    // Container types can have separate type serializers for content (value / element) type
    if (contentTypeSer != null) {
        if (serializationType == null) {
            serializationType = declaredType;
        }
        JavaType ct = serializationType.getContentType();
        if (ct == null) {
            throw new IllegalStateException("Problem trying to create BeanPropertyWriter for property '"
                    + propDef.getName() + "' (of type " + _beanDesc.getType() + "); serialization type " + serializationType + " has no content");
        }
        serializationType = serializationType.withContentTypeHandler(contentTypeSer);
        ct = serializationType.getContentType();
    }

    Object valueToSuppress = null;
    boolean suppressNulls = false;

    JsonInclude.Value inclV = _defaultInclusion.withOverrides(propDef.findInclusion());
    JsonInclude.Include inclusion = inclV.getValueInclusion();
    if (inclusion == JsonInclude.Include.USE_DEFAULTS) {
        inclusion = JsonInclude.Include.ALWAYS;
    }

    // 12-Jul-2016, tatu: [databind#1256] Need to make sure we consider type refinement
    JavaType actualType = (serializationType == null) ? declaredType : serializationType;

    switch (inclusion) {
        case NON_DEFAULT:
            if (_defaultInclusion.getValueInclusion() == JsonInclude.Include.NON_DEFAULT) {
                valueToSuppress = getPropertyDefaultValue(propDef.getName(), am, actualType);
            } else {
                valueToSuppress = getDefaultValue(actualType);
            }
            if (valueToSuppress == null) {
                suppressNulls = true;
            } else {
                if (valueToSuppress.getClass().isArray()) {
                    valueToSuppress = ArrayBuilders.getArrayComparator(valueToSuppress);
                }
            }
            break;
        case NON_ABSENT:
            suppressNulls = true;
            if (actualType != null && actualType.isReferenceType()) {
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
        case NON_EMPTY:
            suppressNulls = true;
            valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            break;
        case NON_NULL:
            suppressNulls = true;
            // fall through
        case ALWAYS:
        default:
            if (actualType != null && actualType.isContainerType() && !_config.isEnabled(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)) {
                valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
            }
            break;
    }
    BeanPropertyWriter bpw = new BeanPropertyWriter(propDef,
            am, _beanDesc.getClassAnnotations(), declaredType,
            ser, typeSer, serializationType, suppressNulls, valueToSuppress);

    // How about custom null serializer?
    Object serDef = _annotationIntrospector.findNullSerializer(am);
    if (serDef != null) {
        bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
    }
    // And then, handling of unwrapping
    NameTransformer unwrapper = _annotationIntrospector.findUnwrappingNameTransformer(am);
    if (unwrapper != null) {
        bpw = bpw.unwrappingWriter(unwrapper);
    }
    return bpw;
}