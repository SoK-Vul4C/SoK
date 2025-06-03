@SuppressWarnings({"unchecked", "rawtypes"})
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    if (lexer.token() == JSONToken.NULL) {
        lexer.nextToken(JSONToken.COMMA);
        return null;
    }

    if (lexer.token() == JSONToken.LITERAL_STRING) {
        byte[] bytes = lexer.bytesValue();
        lexer.nextToken(JSONToken.COMMA);
        return (T) bytes; // Assuming this is a valid conversion to the target type
    }

    JSONArray array = new JSONArray();
    parser.parseArray(array, fieldName);

    if (type instanceof Class) {
        Class<?> clazz = (Class<?>) type;
        if (clazz.isArray()) {
            return (T) JSONArray.toArray(array, clazz.getComponentType());
        } else {
            return array.toJavaObject(clazz);
        }
    } else if (type instanceof ParameterizedType) {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (rawType instanceof Class && List.class.isAssignableFrom((Class<?>) rawType)) {
            return (T) array.toJavaList((Class<?>) actualTypeArguments[0]);
        } else {
            // Handle other cases based on the actual type arguments
            // This is a generic approach and can be adjusted based on specific requirements
            // You may need to implement custom logic for different parameterized types
            // For simplicity, this returns null here
            return null;
        }
    }

    // Handle other cases or return null if type is not supported
    return null;
}