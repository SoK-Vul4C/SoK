public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    // see if we have a policy for this tag
    String tagNameLowerCase = element.localpart.toLowerCase();
    Tag tag = policy.getTagByLowercaseName(tagNameLowerCase);

    // Handle the automatic translation of <param> to nested <embed> for IE
    boolean masqueradingParam = false;
    String embedName = null;
    String embedValue = null;
    if (tag == null && isValidateParamAsEmbed && "param".equals(tagNameLowerCase)) {
        Tag embedPolicy = policy.getEmbedTag();
        if (embedPolicy != null && embedPolicy.isAction(Policy.ACTION_VALIDATE)) {
            tag = embedPolicy;
            masqueradingParam = true;
            embedName = attributes.getValue("name");
            embedValue = attributes.getValue("value");
            XMLAttributes masqueradingAttrs = new XMLAttributesImpl();
            masqueradingAttrs.addAttribute(makeSimpleQname(embedName), "CDATA", embedValue);
            attributes = masqueradingAttrs;
        }
    }

    XMLAttributes validattributes = new XMLAttributesImpl();
    Ops topOp = peekTop();
    if (Ops.REMOVE == topOp || Ops.CSS == topOp) {
        // we are in removal-mode, so remove this tag as well
        // we also remove all child elements of a style element
        this.operations.push(Ops.REMOVE);
    } else if ((tag == null && policy.isEncodeUnknownTag()) || (tag != null && tag.isAction("encode"))) {
        String name = "<" + element.localpart + ">";
        super.characters(new XMLString(name.toCharArray(), 0, name.length()), augs);
        this.operations.push(Ops.FILTER);
    } else if (tag == null) {
        addError(ErrorMessageUtil.ERROR_TAG_NOT_IN_POLICY,
                new Object[]{HTMLEntityEncoder.htmlEntityEncode(element.localpart)});
        this.operations.push(Ops.FILTER);
    } else if (tag.isAction("filter")) {
        addError(ErrorMessageUtil.ERROR_TAG_FILTERED, new Object[]{
                HTMLEntityEncoder.htmlEntityEncode(element.localpart)
        });
        this.operations.push(Ops.FILTER);
    } else if (tag.isAction("validate")) {
        if ("style".equals(element.localpart)) {
            this.operations.push(Ops.CSS);
            cssContent = new StringBuffer();
            cssAttributes = attributes;
        } else {
            // validate all attributes, we need to do this now to find out how to deal with the element
            boolean removeTag = false;
            boolean filterTag = false;
            for (int i = 0; i < attributes.getLength(); i++) {
                // attribute validation logic
            }

            // remaining logic for tag processing
        }
    } else if (tag.isAction("truncate")) {
        this.operations.push(Ops.TRUNCATE);
    } else {
        // no options left, so the tag will be removed
        addError(ErrorMessageUtil.ERROR_TAG_DISALLOWED, new Object[]{
                HTMLEntityEncoder.htmlEntityEncode(element.localpart)
        });
        this.operations.push(Ops.REMOVE);
    }

    // now we know exactly what to do, let's do it
    if (Ops.TRUNCATE.equals(operations.peek())) {
        // copy the element, but remove all attributes
        super.startElement(element, new XMLAttributesImpl(), augs);
    } else if (Ops.KEEP.equals(operations.peek())) {
        // copy the element, but only copy accepted attributes
        super.startElement(element, validattributes, augs);
    }
}