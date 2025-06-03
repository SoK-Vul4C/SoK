protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    if (xmlIn != null) {
        _xmlInputFactory = xmlIn;
    } else {
        _xmlInputFactory = XMLInputFactory.newFactory();
    }

    if (xmlOut != null) {
        _xmlOutputFactory = xmlOut;
    } else {
        _xmlOutputFactory = XMLOutputFactory.newFactory();
    }

    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}