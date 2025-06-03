import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
    return factory.createXMLStreamReader(reader);
}