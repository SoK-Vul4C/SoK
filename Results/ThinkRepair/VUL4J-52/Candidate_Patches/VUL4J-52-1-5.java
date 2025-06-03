@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    // Collect the methods that need to be visited
    List<MethodNode> methodsToVisit = new ArrayList<>();
    for (MethodNode m : classNode.getMethods()) {
        methodsToVisit.add(m);
    }

    // Visit the collected methods
    for (MethodNode m : methodsToVisit) {
        visitor.visitMethod(m);
    }

    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }

    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }
}