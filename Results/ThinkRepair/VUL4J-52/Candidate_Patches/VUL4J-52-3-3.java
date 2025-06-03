@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Process the methods using a copy of the list
    List<MethodNode> methodsCopy = new ArrayList<>(classNode.getMethods());
    for (MethodNode m : methodsCopy) {
        visitor.visitMethod(m);
    }

    // Process the object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Process the fields using a copy of the list
    List<FieldNode> fieldsCopy = new ArrayList<>(classNode.getFields());
    for (FieldNode f : fieldsCopy) {
        visitor.visitField(f);
    }
}