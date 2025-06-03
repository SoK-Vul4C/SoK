@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    // Process constructors first
    processConstructors(visitor, classNode);

    // Visit finalizers and methods overriding finalization separately
    for (MethodNode m : classNode.getMethods()) {
        if (m.getName().equals("finalize") || m.isOverriding()) {
            // Handle finalizers and methods overriding finalization here
            // Add your custom logic or processing for these methods
        }
    }

    // Visit regular methods (excluding finalizers and methods overriding finalization)
    for (MethodNode m : classNode.getMethods()) {
        if (!m.getName().equals("finalize") && !m.isOverriding()) {
            visitor.visitMethod(m);
        }
    }

    // Visit fields
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }

    // Visit object initializer statements
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }
}