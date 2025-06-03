@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    if (classNode.getMethods() != null && visitor != null) {
        for (MethodNode m : classNode.getMethods()) {
            visitor.visitMethod(m);
        }
    }

    if (classNode.getObjectInitializerStatements() != null && visitor != null) {
        for (Statement s : classNode.getObjectInitializerStatements()) {
            s.visit(visitor);
        }
    }

    if (visitor != null) {
        for (FieldNode f : classNode.getFields()) {
            visitor.visitField(f);
        }
    }
}