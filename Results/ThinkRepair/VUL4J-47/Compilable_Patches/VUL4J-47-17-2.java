protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        // Configure the XMLInputFactory to disable external entity expansion and DTD processing
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlIn.setProperty("http://apache.org/xml/features/disallow-doctype-decl", true);
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}