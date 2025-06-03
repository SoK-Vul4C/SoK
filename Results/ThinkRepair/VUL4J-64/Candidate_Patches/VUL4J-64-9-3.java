final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLStreamReader xmlStreamReader = null;
    try {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_COALESCING, true);
        factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

        InputStream processedInputStream = wrapPrefixRemovingInputStream(inputStream);
        xmlStreamReader = factory.createXMLStreamReader(processedInputStream);
    } catch (XMLStreamException | IOException e) {
        // Handle the exception or rethrow it as needed
        // Example: throw new XMLStreamException("Error creating XML stream reader", e);
        e.printStackTrace();
    }
    return xmlStreamReader;
}