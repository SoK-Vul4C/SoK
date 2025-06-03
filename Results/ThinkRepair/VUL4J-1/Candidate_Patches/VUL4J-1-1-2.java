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
        componentClass = determineComponentClass(componentType, parser.getContext().type);
    } else {
        Class clazz = (Class) type;
        componentType = componentClass = clazz.getComponentType();
    }
    
    JSONArray array = new JSONArray();
    parser.parseArray(componentClass, array, fieldName);

    return (T) toObjectArray(parser, componentClass, array);
}

private Class determineComponentClass(Type componentType, Type objType) {
    if (componentType instanceof TypeVariable) {
        TypeVariable typeVar = (TypeVariable) componentType;
        if (objType instanceof ParameterizedType) {
            ParameterizedType objParamType = (ParameterizedType) objType;
            Type objRawType = objParamType.getRawType();
            Type actualType = null;
            if (objRawType instanceof Class) {
                TypeVariable[] objTypeParams = ((Class) objRawType).getTypeParameters();
                for (int i = 0; i < objTypeParams.length; ++i) {
                    if (objTypeParams[i].getName().equals(typeVar.getName())) {
                        actualType = objParamType.getActualTypeArguments()[i];
                    }
                }
            }
            return actualType instanceof Class ? (Class) actualType : Object.class;
        } else {
            return TypeUtils.getClass(typeVar.getBounds()[0]);
        }
    } else {
        return TypeUtils.getClass(componentType);
    }
}