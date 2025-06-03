private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, String path, Map<String, Object> config) throws Exception {
    if (config == null) config = Collections.emptyMap();
    boolean failOnError = (boolean) config.getOrDefault("failOnError", true);
    List<MapResult> result = new ArrayList<>();
    DocumentBuilder documentBuilder = null;
    try {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        FileUtils.checkReadAllowed(url);

        Map<String, Object> headers = (Map) config.getOrDefault("headers", Collections.emptyMap());

        Document doc = null;
        InputStream inputStream = null;

        try {
            inputStream = Util.openInputStream(url, headers, null);
            doc = documentBuilder.parse(inputStream);

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
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    } catch (FileNotFoundException e) {
        if (!failOnError) {
            return Stream.of(new MapResult(Collections.emptyMap()));
        } else {
            throw e;
        }
    } catch (Exception e) {
        if (!failOnError) {
            return Stream.of(new MapResult(Collections.emptyMap()));
        } else {
            throw e;
        }
    } finally {
        if (documentBuilder != null) {
            documentBuilder.reset(); // Close the DocumentBuilder properly
            documentBuilder = null;
        }
    }
    return result.stream();
}