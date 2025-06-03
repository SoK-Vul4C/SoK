if ("style".equals(element.localpart.toLowerCase())) {
    this.operations.push(Ops.CSS);
    cssContent = new StringBuffer();
    cssAttributes = attributes;
}