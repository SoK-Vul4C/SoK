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
        componentType = genericArrayType.getGenericComponentType();
        componentClass = TypeUtils.getClass(componentType);
    } else if (type instanceof Class) {
        componentClass = (Class) type;
    } else {
        throw new JSONException("Unsupported type: " + type);
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    if (byte[].class.isAssignableFrom(componentClass)) {
        return (T) toByteArray(array);
    } else if (Byte[].class.isAssignableFrom(componentClass)) {
        return (T) toWrappedByteArray(array);
    } else if (componentClass.isArray()) {
        return (T) toArray(parser, componentClass, array);
    } else {
        throw new JSONException("Unsupported type: " + componentClass.getName());
    }
}