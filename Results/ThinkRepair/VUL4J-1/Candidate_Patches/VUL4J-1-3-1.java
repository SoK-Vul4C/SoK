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
        componentClass = TypeUtils.getClass(componentType);
    } else if (type instanceof Class<?> && ((Class<?>) type).isArray()) {
        componentClass = ((Class<?>) type).getComponentType();
        JSONArray array = new JSONArray();
        parser.parseArray(componentClass, array, fieldName);
        return (T) toObjectArray(parser, componentClass, array);
    } else {
        throw new JSONException("Not a generic array type or array class type");
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);
    
    return (T) toObjectArray(parser, componentClass, array);
}