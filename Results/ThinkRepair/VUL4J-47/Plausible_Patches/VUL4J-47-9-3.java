protected XmlFactory(ObjectCodec oc, int xpFeatures, int xgFeatures,
                     XMLInputFactory xmlIn, XMLOutputFactory xmlOut,
                     String nameForTextElem) {
    super(oc);
    _xmlParserFeatures = xpFeatures;
    _xmlGeneratorFeatures = xgFeatures;
    _cfgNameForTextElement = nameForTextElem;

    if (xmlIn == null) {
        xmlIn = XMLInputFactory.newInstance();
        try {
            // Disable external entity expansion
            xmlIn.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            xmlIn.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        } catch (IllegalArgumentException e) {
            // Handle IllegalArgumentException
            System.err.println("Error: IllegalArgumentException occurred while setting XMLInputFactory properties: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.err.println("Error: UnsupportedOperationException occurred while setting XMLInputFactory properties: " + e.getMessage());
        } catch (FactoryConfigurationError e) {
            System.err.println("Error: FactoryConfigurationError occurred while setting XMLInputFactory properties: " + e.getMessage());
        }
    }

    if (xmlOut == null) {
        xmlOut = XMLOutputFactory.newInstance();
    }

    _initFactories(xmlIn, xmlOut);
    _xmlInputFactory = xmlIn;
    _xmlOutputFactory = xmlOut;
}