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

    Class componentClass;
    Type componentType;
    if (type instanceof GenericArrayType) {
        GenericArrayType clazz = (GenericArrayType) type;
        componentType = clazz.getGenericComponentType();
        if (componentType instanceof TypeVariable) {
            TypeVariable<?> typeVar = (TypeVariable<?>) componentType;
            componentClass = Object.class; // default to Object class
        } else {
            componentClass = TypeUtils.getClass(componentType);
        }
    } else {
        Class<?> clazz = (Class<?>) type;
        componentType = componentClass = clazz.getComponentType();
    }
    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName); // Fixed Line

    return (T) toObjectArray(parser, componentClass, array);
}