final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws Exception {
    XMLStreamReader xmlStreamReader = null;
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
    
    try {
        InputStream processedInputStream = wrapPrefixRemovingInputStream(inputStream);
        xmlStreamReader = factory.createXMLStreamReader(processedInputStream);
    } catch (XMLStreamException | IOException e) {
        throw new Exception("Error creating XML stream reader", e);
    }
    
    return xmlStreamReader;
}