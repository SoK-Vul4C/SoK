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
            // Handle TypeVariable
            TypeVariable typeVar = (TypeVariable) componentType;
            componentClass = Object.class; // Default to Object class for TypeVariable
        } else {
            // Handle other component types
            componentClass = TypeUtils.getClass(componentType);
        }
    } else if (type instanceof Class && ((Class) type).isArray()) {
        // Handle array case when type is a class
        Class clazz = (Class) type;
        componentClass = clazz.getComponentType();
    } else {
        throw new IllegalArgumentException("Unsupported type: " + type.getTypeName());
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    return (T) toObjectArray(parser, componentClass, array);
}