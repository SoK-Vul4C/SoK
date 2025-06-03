@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    // Process constructors if they exist
    processConstructors(visitor, classNode);

    // Visit methods if they exist
    if (classNode.getMethods() != null) {
        for (MethodNode m : classNode.getMethods()) {
            visitor.visitMethod(m);
        }
    }

    // Visit object initializer statements if they exist
    if (classNode.getObjectInitializerStatements() != null) {
        for (Statement s : classNode.getObjectInitializerStatements()) {
            s.visit(visitor);
        }
    }

    // Visit fields if they exist
    if (classNode.getFields() != null) {
        for (FieldNode f : classNode.getFields()) {
            visitor.visitField(f);
        }
    }
}