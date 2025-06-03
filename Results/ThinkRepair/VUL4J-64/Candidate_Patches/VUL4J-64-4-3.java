final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException, IOException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    InputStream wrappedInputStream = wrapPrefixRemovingInputStream(inputStream);
    return factory.createXMLStreamReader(wrappedInputStream);
}

private static InputStream wrapPrefixRemovingInputStream(InputStream inputStream) {
    // Implement logic to wrap the input stream if needed and return the wrapped stream
    // For demonstration, let's assume a simple pass-through for now
    return inputStream;
}