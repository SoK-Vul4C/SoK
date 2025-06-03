@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Process the methods using an iterator
    Iterator<MethodNode> methodIterator = classNode.getMethods().iterator();
    while (methodIterator.hasNext()) {
        MethodNode m = methodIterator.next();
        visitor.visitMethod(m);
    }

    // Process the object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Process the fields using an iterator
    Iterator<FieldNode> fieldIterator = classNode.getFields().iterator();
    while (fieldIterator.hasNext()) {
        FieldNode f = fieldIterator.next();
        visitor.visitField(f);
    }
}