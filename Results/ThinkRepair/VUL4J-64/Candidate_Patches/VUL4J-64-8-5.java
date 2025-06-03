final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
    
    try {
        return factory.createXMLStreamReader(wrapPrefixRemovingInputStream(inputStream));
    } catch (XMLStreamException e) {
        throw new RuntimeException("Error creating XML stream reader", e);
    }
}