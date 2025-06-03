public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
	// see if we have a policy for this tag.
    String tagNameLowerCase = element.localpart.toLowerCase();
    Tag tag = policy.getTagByLowercaseName(tagNameLowerCase);

	/*
	 * Handle the automatic translation of <param> to nested <embed> for IE.
	 * This is only if the "validateParamAsEmbed" directive is enabled.
	 */
	boolean masqueradingParam = false;
	String embedName = null;
	String embedValue = null;
	if (tag == null && isValidateParamAsEmbed && "param".equals(tagNameLowerCase)) {
		Tag embedPolicy = policy.getEmbedTag();
		if (embedPolicy != null && embedPolicy.isAction(Policy.ACTION_VALIDATE)) {
			tag = embedPolicy;
			masqueradingParam = true;
			// take <param name=x value=y> and turn into
			// <embed x=y></embed>
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

		boolean isStyle = "style".equals(element.localpart);

		if (isStyle) {
			this.operations.push(Ops.CSS);
			cssContent = new StringBuffer();
			cssAttributes = attributes;
		} else {
			// validate all attributes, we need to do this now to find out
			// how to deal with the element
			boolean removeTag = false;
			boolean filterTag = false;
			for (int i = 0; i < attributes.getLength(); i++) {
				String name = attributes.getQName(i);
				String value = attributes.getValue(i);
				String nameLower = name.toLowerCase();
				Attribute attribute = tag.getAttributeByName(nameLower);
				if (attribute == null) {
					// no policy defined, perhaps it is a global attribute
					attribute = policy.getGlobalAttributeByName(nameLower);
				}

				if ("style".equalsIgnoreCase(name)) {
					CssScanner styleScanner = makeCssScanner();
					try {
						CleanResults cr = styleScanner.scanInlineStyle(value, element.localpart, maxInputSize);
						attributes.setValue(i, cr.getCleanHTML());
						validattributes.addAttribute(makeSimpleQname(name), "CDATA", cr.getCleanHTML());
						errorMessages.addAll(cr.getErrorMessages());
					} catch (ScanException e) {
						addError(ErrorMessageUtil.ERROR_CSS_ATTRIBUTE_MALFORMED, new Object[]{
								element.localpart, HTMLEntityEncoder.htmlEntityEncode(value)
						});
					}
				} else if (attribute != null) {
					// validate the values against the policy
					boolean isValid = false;
					if (attribute.containsAllowedValue(value.toLowerCase())) {
						validattributes.addAttribute(makeSimpleQname(name), "CDATA", value);
						isValid = true;
					}

					if (!isValid) {
						isValid = attribute.matchesAllowedExpression(value);
						if (isValid) {
							validattributes.addAttribute(makeSimpleQname(name), "CDATA", value);
						}
					}

					if (!isValid && "removeTag".equals(attribute.getOnInvalid())) {
						addError(ErrorMessageUtil.ERROR_ATTRIBUTE_INVALID_REMOVED,
								new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
						removeTag = true;
					} else if (!isValid && ("filterTag".equals(attribute.getOnInvalid()) || masqueradingParam)) {
						addError(ErrorMessageUtil.ERROR_ATTRIBUTE_CAUSE_FILTER,
								new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
						filterTag = true;
					} else if (!isValid) {
						addError(ErrorMessageUtil.ERROR_ATTRIBUTE_INVALID, new Object[]{tag.getName(), HTMLEntityEncoder.htmlEntityEncode(name), HTMLEntityEncoder.htmlEntityEncode(value)});
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
					validattributes.addAttribute(makeSimpleQname("rel"), "CDATA", "nofollow");
				}

				if (masqueradingParam) {
					validattributes = new XMLAttributesImpl();
					validattributes.addAttribute(makeSimpleQname("name"), "CDATA", embedName);
					validattributes.addAttribute(makeSimpleQname("value"), "CDATA", embedValue);
				}

				this.operations.push(Ops.KEEP);
			}
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