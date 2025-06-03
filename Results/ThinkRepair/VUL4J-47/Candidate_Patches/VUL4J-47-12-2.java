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
    // Set properties on the XMLInputFactory after ensuring it is not null
    if (xmlIn != null) {
        // Configure to ignore DTD processing and disable external entity expansion
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlIn.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
    }
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}