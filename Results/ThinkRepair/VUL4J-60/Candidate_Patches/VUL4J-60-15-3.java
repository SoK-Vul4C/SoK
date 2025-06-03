// Fixed Function
if ("style".equals(element.localpart)) {
    this.operations.push(Ops.CSS);
    cssContent = new StringBuffer();
    cssAttributes = attributes;
}