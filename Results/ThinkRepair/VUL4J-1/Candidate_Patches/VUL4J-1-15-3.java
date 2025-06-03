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
        GenericArrayType genericArrayType = (GenericArrayType) type;
        Type genericComponentType = genericArrayType.getGenericComponentType();
        componentClass = TypeUtils.getClass(genericComponentType);
    } else {
        Class clazz = (Class) type;
        componentClass = clazz.getComponentType();
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    if (componentClass == Integer.class && componentType == Byte.class) {
        Byte[] byteObjectArray = new Byte[array.size()];
        for (int i = 0; i < array.size(); i++) {
            byteObjectArray[i] = ((Integer) array.get(i)).byteValue();
        }
        return (T) byteObjectArray;
    } else if (componentClass.equals(Object.class) && type instanceof ParameterizedType) {
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class componentArrayType = (Class) actualTypeArguments[0];
        Object[] objectArray = array.toArray((Object[]) Array.newInstance(componentArrayType, array.size()));
        return (T) objectArray;
    } else {
        return (T) toObjectArray(parser, componentClass, array);
    }
}