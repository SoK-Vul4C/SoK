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
        
        if (componentType instanceof Class) {
            componentClass = (Class<?>) componentType;
        } else if (componentType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) componentType;
            componentClass = (Class<?>) parameterizedType.getRawType();
        } else {
            componentClass = Object.class;
        }

    } else if (type instanceof Class) {
        Class<?> clazz = (Class<?>) type;
        componentType = componentClass = clazz.getComponentType();
    } else {
        componentClass = Object.class;
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);
    
    if (type instanceof Class) {
        return (T) toJsonArray(parser, array, (Class<T>) type); // Fix the type casting here
    } else {
        return (T) toJsonArray(parser, array, (Class<T>) componentClass); // Fix the type casting here
    }
}