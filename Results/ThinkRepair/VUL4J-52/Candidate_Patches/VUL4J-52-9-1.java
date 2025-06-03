@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }

    for (MethodNode m : classNode.getMethods()) {
        visitor.visitMethod(m);
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }
}