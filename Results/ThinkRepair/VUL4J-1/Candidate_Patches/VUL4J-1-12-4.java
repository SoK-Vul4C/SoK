@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialize(DefaultJSONParser parser, Type type, Object fieldName) {
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

    TypeToken<?> typeToken = TypeToken.get(type);
    Class<?> componentClass = (Class<?>) typeToken.getComponentType().getRawType();

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    return (T) toObjectArray(componentClass, array);
}

private <T> T[] toObjectArray(Class<?> componentClass, JSONArray array) {
    int size = array.size();
    T[] result = (T[]) Array.newInstance(componentClass, size);
    for (int i = 0; i < size; i++) {
        result[i] = (T) array.get(i);
    }
    return result;
}