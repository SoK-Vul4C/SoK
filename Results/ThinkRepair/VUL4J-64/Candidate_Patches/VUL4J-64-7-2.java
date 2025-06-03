final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    Reader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    return factory.createXMLStreamReader(reader);
}