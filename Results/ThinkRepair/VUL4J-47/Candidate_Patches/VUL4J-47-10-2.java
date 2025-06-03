protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        // Disable external entity expansion and DTD processing
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        // Prevent the resolution of external DTD subsets
        try {
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            xmlIn.setProperty("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", 0);
            xmlIn.setProperty("http://apache.org/xml/properties/security-manager", null);
        } catch (IllegalArgumentException | UnsupportedOperationException e) {
            // Ignore exceptions if properties are not supported
        }
    }
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}