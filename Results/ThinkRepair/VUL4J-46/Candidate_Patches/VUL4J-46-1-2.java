protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;
    if (xmlIn == null) {
        this._xmlInputFactory = XMLInputFactory.newInstance();
    } else {
        this._xmlInputFactory = xmlIn;
    }
    if (xmlOut == null) {
        this._xmlOutputFactory = XMLOutputFactory.newInstance();
    } else {
        this._xmlOutputFactory = xmlOut;
    }
    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}