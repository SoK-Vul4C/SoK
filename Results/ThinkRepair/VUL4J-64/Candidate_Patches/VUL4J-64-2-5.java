import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

final static private XMLStreamReader createXMLStreamReader(InputStream inputStream) throws XMLStreamException {
    XMLInputFactory factory = XMLInputFactory.newInstance();
    factory.setProperty(XMLInputFactory.IS_COALESCING, true);
    factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, true);

    try {
        InputStreamReader reader = new InputStreamReader(inputStream);
        return factory.createXMLStreamReader(reader);
    } catch (IOException e) {
        // Handle the IOException here
        e.printStackTrace();
        throw new XMLStreamException(e.getMessage());
    }
}