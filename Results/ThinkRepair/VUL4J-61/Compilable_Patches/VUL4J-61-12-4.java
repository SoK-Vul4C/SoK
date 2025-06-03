import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, @Name("path") String path, Map<String, Object> config) throws Exception {
    if (config == null) config = Collections.emptyMap();
    boolean failOnError = (boolean) config.getOrDefault("failOnError", true);
    List<MapResult> result = new ArrayList<>();
    try {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        FileUtils.checkReadAllowed(url);

        Map<String, Object> headers = (Map<String, Object>) config.getOrDefault("headers", Collections.emptyMap());

        Document doc = documentBuilder.parse(Util.openInputStream(url, headers, null));
        XPathFactory xPathFactory = XPathFactory.newInstance();

        XPath xPath = xPathFactory.newXPath();

        path = StringUtils.isEmpty(path) ? "/" : path;
        XPathExpression xPathExpression = xPath.compile(path);
        NodeList nodeList = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);

        for (int i = 0; i < nodeList.getLength(); i++) {
            final Deque<Map<String, Object>> stack = new LinkedList<>();

            handleNode(stack, nodeList.item(i), simpleMode);
            for (int index = 0; index < stack.size(); index++) {
                result.add(new MapResult(stack.pollFirst()));
            }
        }
    } catch (IOException | ParserConfigurationException | SAXException e) {
        if (!failOnError) {
            return Stream.of(new MapResult(Collections.emptyMap()));
        } else {
            throw e;
        }
    }
    return result.stream();
}