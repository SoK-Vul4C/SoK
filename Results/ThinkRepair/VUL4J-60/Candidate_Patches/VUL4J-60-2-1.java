if ("style".equals(element.localpart.toLowerCase())) {
    this.operations.push(Ops.CSS);
    cssContent = new StringBuilder();
    cssAttributes = attributes;
}