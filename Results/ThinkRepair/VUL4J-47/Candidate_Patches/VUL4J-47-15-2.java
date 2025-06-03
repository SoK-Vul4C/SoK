protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem)
{
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newFactory();
        // Disable external entity expansion and DTD support
        try {
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        } catch (IllegalArgumentException e) {
            // Handle property not supported exception
            System.err.println("Warning: XMLInputFactory property not supported");
        }
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newFactory();
    }
    
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}