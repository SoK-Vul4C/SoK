@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Collect methods into a separate list for safe iteration
    List<MethodNode> methodsToProcess = new ArrayList<>(classNode.getMethods());
    for (MethodNode m : methodsToProcess) {
        visitor.visitMethod(m);
    }

    // Process the object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Collect fields into a separate list for safe iteration
    List<FieldNode> fieldsToProcess = new ArrayList<>(classNode.getFields());
    for (FieldNode f : fieldsToProcess) {
        visitor.visitField(f);
    }
}