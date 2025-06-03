final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    try {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
        
        InputStream wrappedInputStream = wrapPrefixRemovingInputStream(inputStream);
        return factory.createXMLStreamReader(wrappedInputStream);
    } catch (FactoryConfigurationError e) {
        throw new XMLStreamException("Error creating XML stream reader", e);
    }
}