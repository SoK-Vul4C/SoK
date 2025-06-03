@Override
public void call(final SourceUnit source, GeneratorContext context, ClassNode classNode) {
    if (classNode == null) {
        return;
    }

    ClassCodeExpressionTransformer visitor = createVisitor(source, classNode);

    if (visitor == null) {
        // Handle the case where visitor is null
        // e.g., log an error and return
        return;
    }

    // Process constructors
    processConstructors(visitor, classNode);

    // Process methods
    if (classNode.getMethods() != null) {
        for (MethodNode m : classNode.getMethods()) {
            // Skip if method is null
            if (m != null) {
                visitor.visitMethod(m);
            }
        }
    }

    // Process object initializer statements
    if (classNode.getObjectInitializerStatements() != null) {
        for (Statement s : classNode.getObjectInitializerStatements()) {
            // Skip if statement is null
            if (s != null) {
                s.visit(visitor);
            }
        }
    }

    // Process fields
    if (classNode.getFields() != null) {
        for (FieldNode f : classNode.getFields()) {
            // Skip if field is null
            if (f != null) {
                visitor.visitField(f);
            }
        }
    }
}