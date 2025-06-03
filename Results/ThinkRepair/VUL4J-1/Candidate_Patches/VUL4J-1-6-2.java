@SuppressWarnings({"unchecked", "rawtypes"})
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
        GenericArrayType clazz = (GenericArrayType) type;
        componentType = clazz.getGenericComponentType();
        if (componentType instanceof TypeVariable) {
            // Handle TypeVariable to determine actual componentClass
            // Implement this part based on requirements
            componentClass = Object.class; // Example fallback
        } else {
            componentClass = TypeUtils.getClass(componentType);
        }
    } else {
        Class<?> clazz = (Class<?>) type;
        componentType = componentClass = clazz.getComponentType();
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array);

    // Convert elements in the json array to the proper type
    List<Object> objectList = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
        Object element = array.get(i);
        if (element instanceof Number) {
            if (componentClass == Byte.class) {
                objectList.add(((Number) element).byteValue());
            } else if (componentClass == Short.class) {
                objectList.add(((Number) element).shortValue());
            } else if (componentClass == Integer.class) {
                objectList.add(((Number) element).intValue());
            } else if (componentClass == Long.class) {
                objectList.add(((Number) element).longValue());
            } else {
                objectList.add(element);
            }
        } else {
            objectList.add(element);
        }
    }

    return (T) objectList.toArray((Object[]) Array.newInstance(componentClass, objectList.size()));
}