@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Make a copy of the list of methods to avoid modifications during iteration
    List<MethodNode> methods = new ArrayList<>(classNode.getMethods());
    for (MethodNode m : methods) {
        visitor.visitMethod(m);
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    // Make a copy of the list of fields to avoid modifications during iteration
    List<FieldNode> fields = new ArrayList<>(classNode.getFields());
    for (FieldNode f : fields) {
        visitor.visitField(f);
    }
}