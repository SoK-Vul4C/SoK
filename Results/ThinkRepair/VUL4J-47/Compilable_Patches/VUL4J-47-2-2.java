protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        // Disable external entity resolution and DTD processing
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlIn.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
    } // Fixed Line
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn; // Assigning the XMLInputFactory object to the class variable
    _xmlOutputFactory = xmlOut;
}