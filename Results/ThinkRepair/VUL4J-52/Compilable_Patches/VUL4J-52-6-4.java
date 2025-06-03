@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    for (MethodNode method : classNode.getMethods()) {
        try {
            visitor.visitMethod(method);
        } catch (Exception e) {
            // Handle or log the exception for the method
            e.printStackTrace(); // Example: Print the exception
        }
    }

    for (Statement statement : classNode.getObjectInitializerStatements()) {
        try {
            statement.visit(visitor);
        } catch (Exception e) {
            // Handle or log the exception for the statement
            e.printStackTrace(); // Example: Print the exception
        }
    }

    for (FieldNode field : classNode.getFields()) {
        try {
            visitor.visitField(field);
        } catch (Exception e) {
            // Handle or log the exception for the field
            e.printStackTrace(); // Example: Print the exception
        }
    }
}