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

    Class<T> componentClass;
    Type componentType;
    if (type instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();
        if (componentType instanceof Class) {
            return parser.parseArray((Class<T>) componentType, fieldName);
        } else {
            throw new JSONException("Unsupported type: " + type);
        }
    } else if (type instanceof Class && ((Class<?>) type).isArray()) {
        componentClass = (Class<T>) ((Class<?>) type).getComponentType();
        JSONArray array = new JSONArray();
        parser.parseArray(componentClass, array, fieldName);
        return (T) toObjectArray(parser, componentClass, array);
    } else {
        throw new JSONException("Unsupported type: " + type);
    }
}