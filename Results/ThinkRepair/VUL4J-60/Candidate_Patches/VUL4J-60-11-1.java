public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    String tagNameLowerCase = element.localpart.toLowerCase();
    Tag tag = policy.getTagByLowercaseName(tagNameLowerCase);

    if (shouldRemoveElement(tag)) {
        handleRemoveOperation();
    } else if (shouldEncodeUnknownTag(tag, tagNameLowerCase)) {
        handleEncodeOperation(element, augs);
    } else if (tag == null) {
        handleUnknownTag(attributes, element, augs);
    } else {
        handleTagOperations(tag, attributes, element, augs);
    }
}

private boolean shouldRemoveElement(Tag tag) {
    Ops topOp = peekTop();
    return Ops.REMOVE == topOp || Ops.CSS == topOp;
}

private void handleRemoveOperation() {
    this.operations.push(Ops.REMOVE);
}

private boolean shouldEncodeUnknownTag(Tag tag, String tagNameLowerCase) {
    return (tag == null && policy.isEncodeUnknownTag()) || (tag != null && tag.isAction("encode"));
}

private void handleEncodeOperation(QName element, Augmentations augs) {
    String name = "<" + element.localpart + ">";
    super.characters(new XMLString(name.toCharArray(), 0, name.length()), augs);
    this.operations.push(Ops.FILTER);
}

private void handleUnknownTag(XMLAttributes attributes, QName element, Augmentations augs) {
    addError(ErrorMessageUtil.ERROR_TAG_NOT_IN_POLICY, new Object[]{HTMLEntityEncoder.htmlEntityEncode(element.localpart)});
    this.operations.push(Ops.FILTER);
}

private void handleTagOperations(Tag tag, XMLAttributes attributes, QName element, Augmentations augs) {
    if (tag.isAction("filter")) {
        addError(ErrorMessageUtil.ERROR_TAG_FILTERED, new Object[]{HTMLEntityEncoder.htmlEntityEncode(element.localpart)});
        this.operations.push(Ops.FILTER);
    } else if (tag.isAction("validate")) {
        handleValidation(tag, attributes, element);
    } else if (tag.isAction("truncate")) {
        this.operations.push(Ops.TRUNCATE);
    } else {
        // Handle other cases or errors as needed
    }
}

private void handleValidation(Tag tag, XMLAttributes attributes, QName element) {
    boolean isStyle = "style".endsWith(element.localpart);

    if (isStyle) {
        handleStyleOperation(attributes);
    } else {
        validateAttributes(tag, attributes, element);
    }
}

private void handleStyleOperation(XMLAttributes attributes) {
    this.operations.push(Ops.CSS);
    cssContent = new StringBuffer();
    cssAttributes = attributes;
}

private void validateAttributes(Tag tag, XMLAttributes attributes, QName element) {
    XMLAttributes validAttributes = new XMLAttributesImpl();
    boolean removeTag = false;
    boolean filterTag = false;

    for (int i = 0; i < attributes.getLength(); i++) {
        String name = attributes.getQName(i);
        String value = attributes.getValue(i);
        // Validation logic for attributes
        // Add logic to handle attribute validation
    }

    handleAttributeValidationResults(validAttributes, removeTag, filterTag, element);
}

private void handleAttributeValidationResults(XMLAttributes validAttributes, boolean removeTag, boolean filterTag, QName element) {
    if (removeTag) {
        this.operations.push(Ops.REMOVE);
    } else if (filterTag) {
        this.operations.push(Ops.FILTER);
    } else {
        handleAttributesAfterValidation(validAttributes, element);
    }
}

private void handleAttributesAfterValidation(XMLAttributes validAttributes, QName element) {
    if (isNofollowAnchors && "a".equals(element.localpart)) {
        validAttributes.addAttribute(makeSimpleQname("rel"), "CDATA", "nofollow");
    }

    // Handle other attribute processing as needed

    this.operations.push(Ops.KEEP);
}