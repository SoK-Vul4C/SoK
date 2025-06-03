@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) { // TODO is this even possible? CpsTransformer implies it is not.
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    for (MethodNode m : classNode.getMethods()) {
        try {
            // Process each method and handle exceptions
            visitor.visitMethod(m);
        } catch (Exception e) {
            // Handle or log the exception if needed
        }
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        try {
            // Process each initializer statement and handle exceptions
            s.visit(visitor);
        } catch (Exception e) {
            // Handle or log the exception if needed
        }
    }

    for (FieldNode f : classNode.getFields()) {
        try {
            // Process each field and handle exceptions
            visitor.visitField(f);
        } catch (Exception e) {
            // Handle or log the exception if needed
        }
    }
}