protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
        XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
        String nameForTextElem)
{
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    _xmlInputFactory = (xmlIn != null) ? xmlIn : XMLInputFactory.newFactory();
    _xmlOutputFactory = (xmlOut != null) ? xmlOut : XMLOutputFactory.newFactory();

    // Initialize the factories if they were newly created
    if (xmlIn == null) {
        _initFactories(_xmlInputFactory, _xmlOutputFactory);
    }
}