@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    for (MethodNode m : classNode.getMethods()) {
        try {
            visitor.visitMethod(m);
        } catch (Exception e) {
            // Handle or log the exception if needed
            e.printStackTrace(); // Example: Print the exception
        }
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        try {
            s.visit(visitor);
        } catch (Exception e) {
            // Handle or log the exception if needed
            e.printStackTrace(); // Example: Print the exception
        }
    }

    for (FieldNode f : classNode.getFields()) {
        try {
            visitor.visitField(f);
        } catch (Exception e) {
            // Handle or log the exception if needed
            e.printStackTrace(); // Example: Print the exception
        }
    }
}