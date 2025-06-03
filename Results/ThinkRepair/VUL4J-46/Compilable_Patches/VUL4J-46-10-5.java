protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut, String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    // Check and assign default if XML Input Factory is null
    if (xmlIn == null) {
        _xmlInputFactory = XMLInputFactory.newFactory();
    } else {
        _xmlInputFactory = xmlIn;
    }

    // Check and assign default if XML Output Factory is null
    if (xmlOut == null) {
        _xmlOutputFactory = XMLOutputFactory.newFactory();
    } else {
        _xmlOutputFactory = xmlOut;
    }

    // Initialize factories and assign values
    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}