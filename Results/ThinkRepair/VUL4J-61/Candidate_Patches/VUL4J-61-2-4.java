private Stream<MapResult> xmlXpathToMapResult(@Name("url") String url, boolean simpleMode, String path, Map<String, Object> config) {
    if (config == null) {
        config = Collections.emptyMap();
    }
    boolean failOnError = (boolean) config.getOrDefault("failOnError", true);
    List<MapResult> result = new ArrayList<>();
    try {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = Util.openInputStream(url, (Map<String, Object>) config.getOrDefault("headers", Collections.emptyMap()), null);
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

        inputStream.close();
    } catch (FileNotFoundException e) {
        if (!failOnError) {
            result.add(new MapResult(Collections.emptyMap()));
        } else {
            throw new RuntimeException(e);
        }
    } catch (IOException e) {
        if (!failOnError) {
            result.add(new MapResult(Collections.emptyMap()));
        } else {
            throw new RuntimeException(e);
        }
    } catch (Exception e) {
        if (!failOnError) {
            result.add(new MapResult(Collections.emptyMap()));
        } else {
            throw new RuntimeException(e);
        }
    }
    return result.stream();
}