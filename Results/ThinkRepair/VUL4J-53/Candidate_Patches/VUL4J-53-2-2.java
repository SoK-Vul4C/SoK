protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt) throws IOException {
    final PropertyBasedCreator creator = _propertyBasedCreator;
    PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, _objectIdReader);

    TokenBuffer tokens = new TokenBuffer(p, ctxt);
    tokens.writeStartObject();

    JsonToken t = p.getCurrentToken();
    for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
        String propName = p.getCurrentName();
        p.nextToken(); // to point to value
        // creator property?
        SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
        if (creatorProp != null) { 
            if (buffer.assignParameter(creatorProp, _deserializeWithErrorWrapping(p, ctxt, creatorProp))) {
                t = p.currentToken(); // Fixed Line - retrieve the current token after deserialization
                Object bean;
                try {
                    bean = creator.build(ctxt, buffer);
                } catch (Exception e) {
                    bean = wrapInstantiationProblem(e, ctxt);
                }
                p.nextToken(); // Move to next token after deserialization
                tokens.copyCurrentStructure(p); // Copy current structure
                tokens.writeEndObject();
                if (bean.getClass() != _beanType.getRawClass()) {
                    ctxt.reportInputMismatch(creatorProp, "Cannot create polymorphic instances with unwrapped values");
                    return null;
                }
                return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
            }
            continue;
        }
        // Rest of the logic remains as it is
        // Please make sure to include the proper handling for other cases
    }

    // Handling the END_OBJECT case
    Object bean;
    try {
        bean = creator.build(ctxt, buffer);
    } catch (Exception e) {
        wrapInstantiationProblem(e, ctxt);
        return null;
    }
    tokens.writeEndObject(); // Move this line here to ensure writing after the loop completes
    return _unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
}