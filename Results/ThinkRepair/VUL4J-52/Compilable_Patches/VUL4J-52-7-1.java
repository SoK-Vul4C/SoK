@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) { // TODO is this even possible? CpsTransformer implies it is not.
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    processConstructors(visitor, classNode);
    
    if (classNode.getMethods() != null) {
        for (MethodNode m : classNode.getMethods()) {
            if (m != null) {
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