@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) { // TODO is this even possible? CpsTransformer implies it is not.
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Create a copy of the list of methods to avoid ConcurrentModificationException
    List<MethodNode> methodsCopy = new ArrayList<>(classNode.getMethods());
    for (MethodNode m : methodsCopy) {
        visitor.visitMethod(m);
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Continue visiting fields after visiting methods to avoid potential issues with field initialization
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }
}