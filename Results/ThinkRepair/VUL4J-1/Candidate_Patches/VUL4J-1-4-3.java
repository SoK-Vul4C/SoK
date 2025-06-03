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

    if (!(type instanceof Class && ((Class<?>) type).isArray())) {
        throw new JSONException("Unsupported type: " + type);
    }

    Class<T> componentClass = (Class<T>) ((Class<?>) type).getComponentType();
    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    int size = array.size();
    Object resultArray = Array.newInstance(componentClass, size);
    for (int i = 0; i < size; i++) {
        Object element = array.get(i);
        if (element == null || componentClass.isInstance(element)) {
            Array.set(resultArray, i, element);
        } else {
            throw new JSONException("Type mismatch in array elements");
        }
    }

    return (T) resultArray;
}