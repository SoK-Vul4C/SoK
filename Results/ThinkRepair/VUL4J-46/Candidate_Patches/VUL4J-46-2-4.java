protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem)
{
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    try {
        if (xmlIn == null) {
            xmlIn = XMLInputFactory.newFactory();
        }
    } catch (Exception e) {
        System.err.println("Failed to create XMLInputFactory: " + e.getMessage());
        xmlIn = XMLInputFactory.newInstance(); // Fallback to newInstance()
    }

    try {
        if (xmlOut == null) {
            xmlOut = XMLOutputFactory.newFactory();
        }
    } catch (Exception e) {
        System.err.println("Failed to create XMLOutputFactory: " + e.getMessage());
        xmlOut = XMLOutputFactory.newInstance(); // Fallback to newInstance()
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}