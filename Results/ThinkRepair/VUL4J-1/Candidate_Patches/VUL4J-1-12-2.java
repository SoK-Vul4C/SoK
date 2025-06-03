@SuppressWarnings({ "unchecked", "rawtypes" })
public <T> T deserialize(DefaultJSONParser parser, Type type, Object fieldName) {
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
            componentType = resolveTypeVariable((TypeVariable) componentType, parser.getContext().type);
        }
        componentClass = TypeUtils.getClass(componentType);
    } else {
        Class<?> clazz = (Class<?>) type;
        componentType = componentClass = clazz.getComponentType();
    }

    JSONArray array = new JSONArray();
    parser.parseArray(componentType, array, fieldName);

    return (T) toObjectArray(parser, componentClass, array);
}

private Type resolveTypeVariable(TypeVariable<?> typeVariable, Type objectType) {
    if (objectType instanceof ParameterizedType) {
        ParameterizedType paramType = (ParameterizedType) objectType;
        Type rawType = paramType.getRawType();
        Type[] typeArgs = paramType.getActualTypeArguments();
        TypeVariable<?>[] typeParams = TypeUtils.getClass(rawType).getTypeParameters();

        for (int i = 0; i < typeParams.length; i++) {
            if (typeParams[i].getName().equals(typeVariable.getName())) {
                return typeArgs[i];
            }
        }
    }

    return Object.class;
}

private <T> T[] toObjectArray(DefaultJSONParser parser, Class<?> componentClass, JSONArray array) {
    return (T[]) array.toArray((int size) -> (T[]) Array.newInstance(componentClass, size));
}