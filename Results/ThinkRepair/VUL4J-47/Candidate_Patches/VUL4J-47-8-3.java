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

    // Set properties to disable external entity expansion
    try {
        xmlIn.setProperty(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        xmlIn.setProperty("http://apache.org/xml/features/disallow-doctype-decl", true);
        xmlIn.setProperty("http://xml.org/sax/features/external-general-entities", false);
        xmlIn.setProperty("http://xml.org/sax/features/external-parameter-entities", false);
    } catch (IllegalArgumentException | UnsupportedOperationException | FactoryConfigurationError e) {
        // Handle exceptions by logging or setting default values
        System.err.println("Error setting XMLInputFactory properties: " + e.getMessage());
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}