@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);

    if (classNode.getMethods() != null) {
        for (MethodNode m : classNode.getMethods()) {
            if (m != null && !isFinalizeMethod(m)) {
                visitor.visitMethod(m);
            }
        }
    }

    if (classNode.getObjectInitializerStatements() != null) {
        for (Statement s : classNode.getObjectInitializerStatements()) {
            if (s != null) {
                s.visit(visitor);
            }
        }
    }

    if (classNode.getFields() != null) {
        for (FieldNode f : classNode.getFields()) {
            if (f != null) {
                visitor.visitField(f);
            }
        }
    }
}

private boolean isFinalizeMethod(MethodNode methodNode) {
    // Check if the method is a finalize method
    return methodNode.getName().equals("finalize")
        && methodNode.getParameters().length == 0
        && methodNode.getReturnType().equals(ClassHelper.VOID_TYPE);
}