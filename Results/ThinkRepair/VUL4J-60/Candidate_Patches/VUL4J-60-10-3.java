public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    String tagNameLowerCase = element.localpart.toLowerCase();
    Tag tag = policy.getTagByLowercaseName(tagNameLowerCase);

    boolean isStyle = "style".equals(element.localpart);
    Ops topOp = peekTop();
    
    if (isStyle && !Ops.CSS.equals(topOp)) {
        ops.push(Ops.CSS);
        cssContent = new StringBuffer();
        cssAttributes = attributes;
    } else {
        // Handle other cases based on the existing logic
    }
}