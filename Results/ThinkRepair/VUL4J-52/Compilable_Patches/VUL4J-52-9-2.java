@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    // Visit the field nodes first
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }

    // Process constructors
    processConstructors(visitor, classNode);

    // Visit the methods
    for (MethodNode m : classNode.getMethods()) {
        visitor.visitMethod(m);
    }

    // Visit the object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }
}