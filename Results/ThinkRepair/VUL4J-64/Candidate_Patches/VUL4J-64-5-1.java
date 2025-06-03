final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    // Fixed Line: Wrap the input stream before creating the XMLStreamReader
    return factory.createXMLStreamReader(wrapPrefixRemovingInputStream(inputStream));
}