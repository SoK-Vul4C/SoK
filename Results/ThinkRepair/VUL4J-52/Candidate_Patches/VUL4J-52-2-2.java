@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);
    for (MethodNode m : classNode.getMethods()) {
        // Check if the method body is null and it's not an abstract method
        if (m.getCode() != null || (m.getModifiers() & Opcodes.ACC_ABSTRACT) != 0) {
            visitor.visitMethod(m);
        }
    }
    for (Statement s : classNode.getObjectInitializerStatements()) {
        s.visit(visitor);
    }
    for (FieldNode f : classNode.getFields()) {
        visitor.visitField(f);
    }
}