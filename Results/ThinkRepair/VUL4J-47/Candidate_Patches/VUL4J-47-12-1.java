protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                    XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                    String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
    }
    // Set properties on the XMLInputFactory after ensuring it is not null
    if (xmlIn != null) {
        // Disable external entity expansion by default
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    }
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}