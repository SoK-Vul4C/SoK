@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    if (lexer.token() == JSONToken.NULL) {
        lexer.nextToken(JSONToken.COMMA);
        return null;
    }

    if (lexer.token() == JSONToken.LITERAL_STRING) {
        byte[] bytes = lexer.bytesValue();
        lexer.nextToken(JSONToken.COMMA);
        return (T) bytes;
    }

    if (type instanceof GenericArrayType) {
        Type componentType = ((GenericArrayType) type).getGenericComponentType();
        Class<?> componentClass = TypeUtils.getRawClass(componentType);

        List<Object> list = new ArrayList<>();
        parser.parseArray(componentClass, list, fieldName);

        if (componentClass.equals(Byte.class)) {
            return (T) toByteArray(list);
        }
        
        throw new JSONException("Unsupported component type: " + componentClass.getName());
    } else if (type instanceof Class && ((Class<?>) type).isArray()) {
        Class<?> clazz = (Class<?>) type;
        Class<?> componentClass = clazz.getComponentType();

        List<Object> list = new ArrayList<>();
        parser.parseArray(componentClass, list, fieldName);
        
        if (componentClass.equals(Byte.class)) {
            return (T) toByteArray(list);
        }

        throw new JSONException("Unsupported component type: " + componentClass.getName());
    }

    throw new JSONException("Unsupported type: " + type.getTypeName());
}

private byte[] toByteArray(List<Object> list) {
    byte[] bytes = new byte[list.size()];
    for (int i = 0; i < list.size(); i++) {
        bytes[i] = ((Number) list.get(i)).byteValue();
    }
    return bytes;
}