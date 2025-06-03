protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    
    if (xmlIn == null) {
        try {
            xmlIn = XMLInputFactory.newInstance();
        } catch (Exception e) {
            // Handle exception appropriately, e.g., log and fallback to default
            System.err.println("Failed to create XMLInputFactory: " + e.getMessage());
            xmlIn = XMLInputFactory.newFactory(); // Falls back to the platform default
        }
    }
    if (xmlOut == null) {
        try {
            xmlOut = XMLOutputFactory.newInstance();
        } catch (Exception e) {
            // Handle exception appropriately, e.g., log and fallback to default
            System.err.println("Failed to create XMLOutputFactory: " + e.getMessage());
            xmlOut = XMLOutputFactory.newFactory(); // Falls back to the platform default
        }
    }
    
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}