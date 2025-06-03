@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    if (lexer.token() == JSONToken.NULL) {
        lexer.nextToken(JSONToken.COMMA);
        return null;
    }

    if (!(type instanceof Class<?>)) {
        throw new JSONException("Type must be a Class instance.");
    }

    Class<?> clazz = (Class<?>) type;

    if (!clazz.isArray()) {
        throw new JSONException("Type must be an array type.");
    }

    Class<?> componentClass = clazz.getComponentType();

    List<Object> objectList = new ArrayList<>();
    parser.parseArray(componentClass, objectList);

    Object objectArray = Array.newInstance(componentClass, objectList.size());
    for (int i = 0; i < objectList.size(); i++) {
        Array.set(objectArray, i, objectList.get(i));
    }

    return (T) objectArray;
}