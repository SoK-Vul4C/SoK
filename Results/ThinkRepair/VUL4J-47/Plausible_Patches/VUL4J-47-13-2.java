protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
    }

    // Disable external entity expansion by default
    try {
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    } catch (IllegalArgumentException e) {
        // Ignore if the property is not supported
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    _initFactories(xmlIn, xmlOut);

    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}