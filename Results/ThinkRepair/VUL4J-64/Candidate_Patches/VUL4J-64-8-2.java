final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
    try {
        return factory.createXMLStreamReader(wrapPrefixRemovingInputStream(inputStream));
    } catch (XMLStreamException | IOException e) {
        throw new XMLStreamException("Error creating XML stream reader", e);
    }
}