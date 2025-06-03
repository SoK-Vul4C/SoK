final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);
    XMLStreamReader reader = factory.createXMLStreamReader(wrapPrefixRemovingInputStream(inputStream));
    return new StreamReaderDelegate(reader) {
        @Override
        public int next() throws XMLStreamException {
            return super.next();
        }
    };
}