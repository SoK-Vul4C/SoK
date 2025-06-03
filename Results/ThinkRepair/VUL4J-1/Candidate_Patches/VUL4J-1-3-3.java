@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    if (lexer.token() == JSONToken.NULL) {
        lexer.nextToken(JSONToken.COMMA);
        return null;
    }

    if (!(type instanceof Class)) {
        throw new JSONException("Type must be a Class instance.");
    }

    Class<?> clazz = (Class<?>) type;

    if (!clazz.isArray()) {
        throw new JSONException("Type must be an array type.");
    }

    Class<?> componentClass = clazz.getComponentType();

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array);

    Object[] objectArray = toObjectArray(parser, componentClass, array);

    return (T) objectArray;
}