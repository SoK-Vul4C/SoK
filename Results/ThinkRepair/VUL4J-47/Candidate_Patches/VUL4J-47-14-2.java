protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem)
{
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false); // Disable DTD processing
        xmlIn.setProperty("http://apache.org/xml/properties/internal/entity-expansion-limit", 0); // Limit entity expansion
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}