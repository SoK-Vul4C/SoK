protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    this._xmlParserFeatures = xpFeatures;
    this._xmlGeneratorFeatures = xgFeatures;
    this._cfgNameForTextElement = nameForTextElem;

    if (xmlIn != null) {
        this._xmlInputFactory = xmlIn;
    } else {
        this._xmlInputFactory = XMLInputFactory.newFactory();
    }
    
    if (xmlOut != null) {
        this._xmlOutputFactory = xmlOut;
    } else {
        this._xmlOutputFactory = XMLOutputFactory.newFactory();
    }

    _initFactories(this._xmlInputFactory, this._xmlOutputFactory);
}