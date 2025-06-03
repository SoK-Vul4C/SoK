import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, String path, Map<String, Object> config) {
    if (config == null) {
        config = Collections.emptyMap();
    }
    boolean failOnError = (boolean) config.getOrDefault("failOnError", true);
    List<MapResult> result = new ArrayList<>();
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    documentBuilderFactory.setNamespaceAware(true);
    documentBuilderFactory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder documentBuilder = null;

    try {
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        FileUtils.checkReadAllowed(url);

        Map<String, Object> headers = (Map<String, Object>) config.getOrDefault("headers", Collections.emptyMap());

        try (InputStream inputStream = Util.openInputStream(url, headers, null)) {
            Document doc = documentBuilder.parse(inputStream);
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
        }
    } catch (FileNotFoundException e) {
        if (!failOnError) {
            return Stream.of(new MapResult(Collections.emptyMap()));
        } else {
            throw new RuntimeException(e);
        }
    } catch (Exception e) {
        if (!failOnError) {
            return Stream.of(new MapResult(Collections.emptyMap()));
        } else {
            throw new RuntimeException(e);
        }
    } finally {
        if (documentBuilder != null) {
            documentBuilder.reset();
        }
    }

    return result.stream();
}