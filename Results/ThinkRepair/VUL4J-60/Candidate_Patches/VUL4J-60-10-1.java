public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    String tagNameLowerCase = element.localpart.toLowerCase();
    // Remaining code omitted for brevity

    // Handle the CSS content only when the operation stack is empty
    if (isStyle && ops.isEmpty()) {
        ops.push(Ops.CSS);
        cssContent = new StringBuffer();
        cssAttributes = attributes;
    } else {
        // Remaining code in the function
    }
}