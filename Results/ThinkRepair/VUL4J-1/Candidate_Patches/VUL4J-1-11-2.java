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

    Class<?> componentClass;
    Type componentType;
    if (type instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();
        if (componentType instanceof TypeVariable) {
            componentClass = Object.class; // Default to Object class
        } else {
            componentClass = TypeUtils.getClass(componentType);
        }
    } else {
        Class<?> clazz = (Class<?>) type;
        componentType = componentClass = clazz.getComponentType();
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    Object[] objArray = (Object[]) Array.newInstance(componentClass, array.size());
    for (int i = 0; i < array.size(); i++) {
        Object obj = array.get(i);
        if (obj instanceof Number) {
            if (componentClass == Byte.class) {
                objArray[i] = ((Number) obj).byteValue();
            } else if (componentClass == Short.class) {
                objArray[i] = ((Number) obj).shortValue();
            } else if (componentClass == Integer.class) {
                objArray[i] = ((Number) obj).intValue();
            } else if (componentClass == Long.class) {
                objArray[i] = ((Number) obj).longValue();
            } else if (componentClass == Float.class) {
                objArray[i] = ((Number) obj).floatValue();
            } else if (componentClass == Double.class) {
                objArray[i] = ((Number) obj).doubleValue();
            } else {
                objArray[i] = obj;
            }
        } else {
            objArray[i] = obj;
        }
    }

    return (T) objArray;
}