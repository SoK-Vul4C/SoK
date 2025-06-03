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
        GenericArrayType genericArrayType = (GenericArrayType) type;
        componentType = genericArrayType.getGenericComponentType();
        if (componentType instanceof TypeVariable) {
            // Handle TypeVariable to determine actual componentClass
            // Implement this part based on requirements
            componentClass = Object.class; // Example fallback
        } else {
            componentClass = TypeUtils.getClass(componentType);
        }
    } else {
        if (type instanceof Class) {
            componentClass = ((Class<?>) type).getComponentType();
        } else {
            throw new JSONException("Unsupported type: " + type.getTypeName());
        }
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array); // Assuming parseArray method is updated

    List<Object> objectList = new ArrayList<>();
    for (int i = 0; i < array.size(); i++) {
        Object element = array.get(i);
        // Perform proper type conversion based on componentClass
        if (element instanceof Number) {
            if (componentClass == Byte.class) {
                objectList.add(((Number) element).byteValue());
            } else if (componentClass == Short.class) {
                objectList.add(((Number) element).shortValue());
            } else if (componentClass == Integer.class) {
                objectList.add(((Number) element).intValue());
            } else if (componentClass == Long.class) {
                objectList.add(((Number) element).longValue());
            } // Add more cases for other numeric types if needed
        } else {
            objectList.add(element);
        }
    }

    // Convert List to array
    Object[] objectArray = (Object[]) Array.newInstance(componentClass, objectList.size());
    for (int i = 0; i < objectList.size(); i++) {
        objectArray[i] = objectList.get(i);
    }

    return (T) objectArray;
}