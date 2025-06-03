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
    Type componentType = null;

    if (type instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();
        componentClass = TypeUtils.getClass(componentType);
    } else {
        Class<?> clazz = (Class<?>) type;
        componentClass = clazz.getComponentType();
        componentType = componentClass;
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    Object[] objectArray = toObjectArray(parser, componentClass, array);
    
    if (componentType instanceof Class && !componentClass.isAssignableFrom((Class<?>) componentType)) {
        Object[] specialTypeArray = convertToSpecialComponentType((Class<?>) componentType, objectArray);
        return (T) specialTypeArray;
    }

    return (T) objectArray;
}