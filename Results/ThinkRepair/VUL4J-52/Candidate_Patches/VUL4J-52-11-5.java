@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Create a copy of the list of methods
    List<MethodNode> methodsCopy = new ArrayList<>(classNode.getMethods());

    Iterator<MethodNode> methodIterator = methodsCopy.iterator();
    while(methodIterator.hasNext()) {
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