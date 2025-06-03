final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    InputStreamReader reader = new InputStreamReader(wrapPrefixRemovingInputStream(inputStream));
    XMLStreamReader xmlStreamReader = factory.createXMLStreamReader(reader);

    return xmlStreamReader;
}