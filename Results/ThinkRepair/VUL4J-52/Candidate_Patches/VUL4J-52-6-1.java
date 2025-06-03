@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) { // TODO is this even possible? CpsTransformer implies it is not.
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);
    
    // Process each method individually and handle exceptions
    for (MethodNode m : classNode.getMethods()) {
        try {
            visitor.visitMethod(m);
        } catch (Exception e) {
            // Handle or log the exception if needed
        }
    }

    // Process object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Process each field individually and handle exceptions
    for (FieldNode f : classNode.getFields()) {
        try {
            visitor.visitField(f);
        } catch (Exception e) {
            // Handle or log the exception if needed
        }
    }
}