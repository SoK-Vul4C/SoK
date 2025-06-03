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
        try {
            // Disable external entity processing
            xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, Boolean.FALSE);
            
            // Disable DTD loading
            xmlIn.setProperty("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            
            // Prevent DTD processing
            xmlIn.setXMLResolver((publicID, systemID, baseURI, namespace) -> new InputSource(new StringReader("")));            
        } catch (Exception e) {
            // Handle any exceptions that may occur during property setting
            e.printStackTrace();
            // You may choose to log the exception or take other actions here
        }
    }
    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }
    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}