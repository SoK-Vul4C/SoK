private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, String path, Map<String, Object> config) throws Exception {
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
    } catch (ParserConfigurationException e) {
        throw new Exception("Error creating document builder: " + e.getMessage(), e);
    }

    try {
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

            while (!stack.isEmpty()) {
                result.add(new MapResult(stack.pollFirst()));
            }
        }
    } catch (IOException e) {
        if (failOnError) {
            throw new Exception("Error processing XML: " + e.getMessage(), e);
        }
        result.add(new MapResult(Collections.emptyMap()));
    }
    return result.stream();
}