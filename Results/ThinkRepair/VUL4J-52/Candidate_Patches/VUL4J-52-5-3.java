@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    // Create the visitor with the source and classNode
    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Visit each method in the class node
    for (MethodNode m : classNode.getMethods()) {
        m.accept(visitor); // Visit the method
    }

    // Visit object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Visit each field in the class node
    for (FieldNode f : classNode.getFields()) {
        f.accept(visitor); // Visit the field
    }
}