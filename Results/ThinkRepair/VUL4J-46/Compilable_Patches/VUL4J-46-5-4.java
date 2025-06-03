protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                 XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                 String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    _xmlInputFactory = (xmlIn != null) ? xmlIn : XMLInputFactory.newInstance();
    _xmlOutputFactory = (xmlOut != null) ? xmlOut : XMLOutputFactory.newInstance();

    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}