@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
    final JSONLexer lexer = parser.lexer;
    if (lexer.token() == JSONToken.NULL) {
        lexer.nextToken(JSONToken.COMMA);
        return null;
    }

    if (lexer.token() == JSONToken.LITERAL_STRING) {
        String stringValue = lexer.stringVal();
        lexer.nextToken(JSONToken.COMMA);

        if (type == byte[].class) {
            return (T) stringValue.getBytes();
        } else {
            return (T) stringValue;
        }
    }

    Class<?> componentClass;
    Type componentType;
    if (type instanceof GenericArrayType) {
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();

        if (componentType instanceof Class) {
            componentClass = (Class<?>) componentType;
        } else {
            throw new JSONException("Unsupported component type: " + componentType);
        }
    } else if (type instanceof Class) {
        Class<?> clazz = (Class<?>) type;
        componentClass = clazz.getComponentType();
    } else {
        throw new JSONException("Unsupported array type: " + type);
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    List<Object> parsedList = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
        Object element = array.get(i);
        if (element instanceof Integer && componentClass == Byte.class) {
            parsedList.add(((Integer) element).byteValue());
        } else {
            parsedList.add(element);
        }
    }

    Object[] parsedArray = parsedList.toArray((Object[]) Array.newInstance(componentClass, parsedList.size()));
    return (T) parsedArray;
}