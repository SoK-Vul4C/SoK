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

    XMLAttributes validAttributes = new XMLAttributesImpl();
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
        addError(ErrorMessageUtil.ERROR_TAG_FILTERED, new Object[]{
                HTMLEntityEncoder.htmlEntityEncode(element.localpart)
        });
        this.operations.push(Ops.FILTER);
    } else if (tag.isAction("validate")) {
        boolean isStyle = "style".equals(element.localpart);

        if (isStyle) {
            this.operations.push(Ops.CSS);
            cssContent = new StringBuffer();
            cssAttributes = attributes;
        } else {
            boolean removeTag = false;
            boolean filterTag = false;
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);
                String value = attributes.getValue(i);
                String nameLower = name.toLowerCase();
                Attribute attribute = tag.getAttributeByName(nameLower);
                if (attribute == null) {
                    attribute = policy.getGlobalAttributeByName(nameLower);
                }

                boolean isValid = false;
                if ("style".equalsIgnoreCase(name)) {
                    CssScanner styleScanner = makeCssScanner();
                    try {
                        CleanResults cr = styleScanner.scanInlineStyle(value, element.localpart, maxInputSize);
                        attributes.setValue(i, cr.getCleanHTML());
                        validAttributes.addAttribute(makeSimpleQname(name), "CDATA", cr.getCleanHTML());
                        errorMessages.addAll(cr.getErrorMessages());
                        isValid = true;
                    } catch (ScanException e) {
                        addError(ErrorMessageUtil.ERROR_CSS_ATTRIBUTE_MALFORMED, new Object[]{
                                element.localpart, HTMLEntityEncoder.htmlEntityEncode(value)
                        });
                    }
                } else if (attribute != null) {
                    isValid = attribute.containsAllowedValue(value.toLowerCase()) || attribute.matchesAllowedExpression(value);

                    if (!isValid) {
                        if ("removeTag".equals(attribute.getOnInvalid())) {
                            addError(ErrorMessageUtil.ERROR_ATTRIBUTE_INVALID_REMOVED,
                                    new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
                            removeTag = true;
                        } else if ("filterTag".equals(attribute.getOnInvalid()) || masqueradingParam) {
                            addError(ErrorMessageUtil.ERROR_ATTRIBUTE_CAUSE_FILTER,
                                    new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
                            filterTag = true;
                        } else {
                            addError(ErrorMessageUtil.ERROR_ATTRIBUTE_INVALID, new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
                        }
                    }

                } else {
                    addError(ErrorMessageUtil.ERROR_ATTRIBUTE_NOT_IN_POLICY, new Object[]{
                            element.localpart, HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)
                    });
                    if (masqueradingParam) {
                        filterTag = true;
                    }
                }
            }

            if (removeTag) {
                this.operations.push(Ops.REMOVE);
            } else if (filterTag) {
                this.operations.push(Ops.FILTER);
            } else {
                if (isNofollowAnchors && "a".equals(element.localpart)) {
                    validAttributes.addAttribute(makeSimpleQname("rel"), "CDATA", "nofollow");
                }

                if (masqueradingParam) {
                    validAttributes = new XMLAttributesImpl();
                    validAttributes.addAttribute(makeSimpleQname("name"), "CDATA", embedName);
                    validAttributes.addAttribute(makeSimpleQname("value"), "CDATA", embedValue);
                }

                this.operations.push(Ops.KEEP);
            }
        }
    } else if (tag.isAction("truncate")) {
        this.operations.push(Ops.TRUNCATE);
    } else {
        addError(ErrorMessageUtil.ERROR_TAG_DISALLOWED, new Object[]{
                HTMLEntityEncoder.htmlEntityEncode(element.localpart)
        });
        this.operations.push(Ops.REMOVE);
    }

    if (Ops.TRUNCATE.equals(operations.peek())) {
        super.startElement(element, new XMLAttributesImpl(), augs);
    } else if (Ops.KEEP.equals(operations.peek())) {
        super.startElement(element, validAttributes, augs);
    }
}