protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
    final PropertyBasedCreator creator = _propertyBasedCreator;
    PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, _objectIdReader);

    TokenBuffer tokens = new TokenBuffer(p, ctxt);
    tokens.writeStartObject();

    JsonToken t = p.getCurrentToken();
    while (t == JsonToken.FIELD_NAME) {
        String propName = p.getCurrentName();
        p.nextToken(); // Move to value
        SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
        if (creatorProp != null) {
            if (buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
                t = p.getCurrentToken(); // Retrieve the current token
                Object bean;
                try {
                    bean = creator.build(ctxt, buffer);
                } catch (Exception e) {
                    bean = wrapInstantiationProblem(e, ctxt);
                }
                tokens.copyCurrentStructure(p); // Copy current field value
                if (bean.getClass() != _beanType.getRawClass()) {
                    ctxt.reportInputMismatch(creatorProp, "Cannot create polymorphic instances with unwrapped values");
                    return null;
                }
                p.nextToken(); // Move to next token
                tokens.writeEndObject();
                return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
            }
        } else {
            // Handle other properties if needed
        }
        
        t = p.nextToken(); // Move to the next token
    }

    // Handling the END_OBJECT case
    Object bean;
    try {
        bean = creator.build(ctxt, buffer);
    } catch (Exception e) {
        wrapInstantiationProblem(e, ctxt);
        return null;
    }
    tokens.writeEndObject();

    return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
}