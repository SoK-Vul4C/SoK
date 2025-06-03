protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    this._xmlParserFeatures = xpFeatures;
    this._xmlGeneratorFeatures = xgFeatures;
    this._cfgNameForTextElement = nameForTextElem;
    
    this._xmlInputFactory = (xmlIn != null) ? xmlIn : XMLInputFactory.newInstance();
    this._xmlOutputFactory = (xmlOut != null) ? xmlOut : XMLOutputFactory.newInstance();
    
    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}