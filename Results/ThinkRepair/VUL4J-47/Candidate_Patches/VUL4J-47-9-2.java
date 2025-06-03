protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        try {
            // Disable external entity expansion
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        } catch (IllegalArgumentException | IOException e) {
            // Handle exceptions if setting properties fails
            // You can log the exception or take appropriate action
            e.printStackTrace();
        }
    }
  
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}