@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Visit and process methods within the class node using the visitor
    if (visitor != null) {
        for (MethodNode m : classNode.getMethods()) {
            m.visit(visitor);
        }
    }

    // Visit object initializer statements
    if (visitor != null) {
        for (Statement s : classNode.getObjectInitializerStatements()) {
            s.visit(visitor);
        }
    }

    // Visit and process fields within the class node using the visitor
    if (visitor != null) {
        for (FieldNode f : classNode.getFields()) {
            f.visit(visitor);
        }
    }
}