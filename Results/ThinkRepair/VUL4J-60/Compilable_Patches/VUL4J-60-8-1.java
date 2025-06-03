public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
    String tagNameLowerCase = element.localpart.toLowerCase();
    Tag tag = policy.getTagByLowercaseName(tagNameLowerCase);

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
        addError(ErrorMessageUtil.ERROR_TAG_FILTERED, new Object[]{HTMLEntityEncoder.htmlEntityEncode(element.localpart)});
        this.operations.push(Ops.FILTER);
    } else if (tag.isAction("validate")) {
        boolean isStyle = "style".equals(element.localpart);

        if (isStyle) {
            this.operations.push(Ops.CSS);
            cssContent = new StringBuffer();
            cssAttributes = attributes;
        } else {
            // validate all attributes
            // handle other cases accordingly
        }
    } else if (tag.isAction("truncate")) {
        this.operations.push(Ops.TRUNCATE);
    } else {
        addError(ErrorMessageUtil.ERROR_TAG_DISALLOWED, new Object[]{HTMLEntityEncoder.htmlEntityEncode(element.localpart)});
        this.operations.push(Ops.REMOVE);
    }

    // handle other cases accordingly
}