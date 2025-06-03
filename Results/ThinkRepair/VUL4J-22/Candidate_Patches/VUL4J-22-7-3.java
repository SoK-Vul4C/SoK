public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if ("/".equals(contextPath) || "".equals(contextPath)) {
        contextPath = ""; // Adjust contextPath to an empty string
    }
    return decodeRequestString(request, contextPath);
}