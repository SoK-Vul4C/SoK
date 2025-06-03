@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Visit and process methods within the class node using the visitor
    for (MethodNode m : classNode.getMethods()) {
        if (visitor != null) {
            visitor.visitMethod(m);
        }
    }

    // Visit object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Visit and process fields within the class node using the visitor
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }
}