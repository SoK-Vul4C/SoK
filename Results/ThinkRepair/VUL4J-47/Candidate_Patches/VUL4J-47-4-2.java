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
    }
    
    // Configure the XMLInputFactory properties to enhance security
    xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
    xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
    xmlIn.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
    
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}