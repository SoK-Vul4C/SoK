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

    Type componentType;
    Class componentClass;
    if (type instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();
        componentClass = TypeUtils.getClass(componentType);
    } else {
        componentClass = TypeUtils.getClass(type);
        componentType = componentClass;
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    Object[] objectArray = toObjectArray(parser, componentClass, array);
    return (T) convertArrayToObjectArray(objectArray, componentClass);
}

private Object[] convertArrayToObjectArray(Object[] objectArray, Class componentClass) {
    int length = objectArray.length;
    Object[] result = (Object[]) Array.newInstance(componentClass, length);
    for (int i = 0; i < length; i++) {
        result[i] = convertToType(objectArray[i], componentClass);
    }
    return result;
}

private Object convertToType(Object obj, Class<?> targetClass) {
    if (obj instanceof Number && targetClass == Byte.class) {
        return ((Number) obj).byteValue();
    } else {
        return targetClass.cast(obj);
    }
}