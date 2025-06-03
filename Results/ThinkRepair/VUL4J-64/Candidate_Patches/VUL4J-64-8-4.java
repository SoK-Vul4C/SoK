final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
    
    try {
        return factory.createXMLStreamReader(wrapPrefixRemovingInputStream(inputStream));
    } catch (XMLStreamException e) {
        // Handle the XMLStreamException by rethrowing it or handling it appropriately
        throw e;
    }
}