@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    Iterator<MethodNode> methodIterator = classNode.getMethods().iterator();
    while (methodIterator.hasNext()) {
        MethodNode m = methodIterator.next();
        visitor.visitMethod(m);
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }
}