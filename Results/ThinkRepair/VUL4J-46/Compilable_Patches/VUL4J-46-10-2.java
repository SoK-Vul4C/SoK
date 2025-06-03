protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures, XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    // Check and assign default if XML Input Factory is null
    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
    }

    // Check and assign default if XML Output Factory is null
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    // Initialize factories and assign values
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
    _initFactories(_xmlInputFactory, _xmlOutputFactory);
}