protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        // as per [dataformat-xml#190], disable external entity expansion by default
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    } // Fixed Line
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn; // Assigning the XMLInputFactory object to the class variable
    _xmlOutputFactory = xmlOut;
}