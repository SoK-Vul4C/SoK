if ("style".equalsIgnoreCase(element.localpart)) {
    this.operations.push(Ops.CSS);
    cssContent = new StringBuffer();
    cssAttributes = attributes;
}