@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Process the methods
    List<MethodNode> methodsToProcess = new ArrayList<>(classNode.getMethods());
    for (MethodNode m : methodsToProcess) {
        visitor.visitMethod(m);
    }

    // Process the object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Process the fields
    List<FieldNode> fieldsToProcess = new ArrayList<>(classNode.getFields());
    for (FieldNode f : fieldsToProcess) {
        visitor.visitField(f);
    }
}