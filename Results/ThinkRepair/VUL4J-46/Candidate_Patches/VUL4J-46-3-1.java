protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        _xmlInputFactory = XMLInputFactory.newInstance();
    }
    if (xmlOut == null) {
        _xmlOutputFactory = XMLOutputFactory.newInstance();
    }
    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}