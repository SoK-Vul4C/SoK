@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    // Process constructors
    processConstructors(visitor, classNode);

    // Visit methods, including handling finalizers and methods overriding finalization
    for (MethodNode m : classNode.getMethods()) {
        if (m.isStaticConstructor() || m.isConstructor()) {
            visitor.visitMethod(m);
        } else if (m.getName().equals("finalize") || m.isOverriding()) {
            // Handle finalizers or methods overriding finalization
            // You may need special processing or marking for these methods
            // Add your custom logic here
        } else {
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