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

    Class<?> componentClass;
    Type componentType;
    if (type instanceof GenericArrayType) {
        componentType = TypeToken.get(type).getTypes().get(0).getType();
        componentClass = TypeUtils.getClass(componentType);
    } else {
        TypeToken<?> typeToken = TypeToken.get(type);
        componentType = typeToken.getRawType().getComponentType();
        componentClass = TypeUtils.getClass(componentType);
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentType, array, fieldName);

    return (T) toObjectArray(parser, componentClass, array);
}

private <T> T[] toObjectArray(DefaultJSONParser parser, Class<?> componentClass, JSONArray array) {
    int size = array.size();
    T[] result = (T[]) Array.newInstance(componentClass, size);
    for (int i = 0; i < size; i++) {
        result[i] = (T) array.get(i);
    }
    return result;
}