protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    
    if (xmlIn == null) {
        try {
            xmlIn = XMLInputFactory.newFactory();
        } catch (Exception e) {
            System.err.println("Failed to create XMLInputFactory: " + e.getMessage());
            xmlIn = XMLInputFactory.newInstance(); // Fallback to default if an exception occurs
        }
    }
    if (xmlOut == null) {
        try {
            xmlOut = XMLOutputFactory.newFactory();
        } catch (Exception e) {
            System.err.println("Failed to create XMLOutputFactory: " + e.getMessage());
            xmlOut = XMLOutputFactory.newInstance(); // Fallback to default if an exception occurs
        }
    }
    
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}