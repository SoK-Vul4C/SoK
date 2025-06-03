private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, String path, Map<String, Object> config) throws Exception {
    if (config == null) {
        config = Collections.emptyMap();
    }
    boolean failOnError = (boolean) config.getOrDefault("failOnError", true);
    List<MapResult> result = new ArrayList<>();
    
    DocumentBuilder documentBuilder = null;
    try {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
        if (!failOnError) {
            result.add(new MapResult(Collections.emptyMap()));
            return result.stream();
        } else {
            throw e;
        }
    }
    
    FileUtils.checkReadAllowed(url);

    Map<String, Object> headers = (Map<String, Object>) config.getOrDefault("headers", Collections.emptyMap());

    try {
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
    } catch (IOException | SAXException | XPathExpressionException e) {
        if (!failOnError) {
            result.clear(); // Clear the list before adding the empty map
            result.add(new MapResult(Collections.emptyMap()));
        } else {
            throw e;
        }
    }
    
    return result.stream();
}