final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    // Wrap the input stream before creating the XMLStreamReader
    InputStream wrappedInputStream = wrapPrefixRemovingInputStream(inputStream);
    
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    return factory.createXMLStreamReader(wrappedInputStream);
}