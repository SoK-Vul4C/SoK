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
        System.err.println("Failed to create XMLInputFactory using newFactory: " + e.getMessage());
    }

    if (xmlIn == null) {
        try {
            xmlIn = XMLInputFactory.newInstance();
        } catch (Exception e) {
            System.err.println("Failed to create XMLInputFactory using newInstance: " + e.getMessage());
        }
    }

    try {
        if (xmlOut == null) {
            xmlOut = XMLOutputFactory.newFactory();
        }
    } catch (Exception e) {
        System.err.println("Failed to create XMLOutputFactory using newFactory: " + e.getMessage());
    }

    if (xmlOut == null) {
        try {
            xmlOut = XMLOutputFactory.newInstance();
        } catch (Exception e) {
            System.err.println("Failed to create XMLOutputFactory using newInstance: " + e.getMessage());
        }
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}