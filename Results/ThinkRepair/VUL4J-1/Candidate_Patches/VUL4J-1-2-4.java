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

    Class<?> componentClass = null;
    if (type instanceof Class<?>) {
        componentClass = ((Class<?>) type).getComponentType();
    } else {
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type rawType = parameterizedType.getRawType();
        if (rawType instanceof Class<?>) {
            componentClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
    }

    if (componentClass == null) {
        throw new JSONException("Unsupported type: " + type);
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    return (T) toArray(parser, componentClass, array);
}