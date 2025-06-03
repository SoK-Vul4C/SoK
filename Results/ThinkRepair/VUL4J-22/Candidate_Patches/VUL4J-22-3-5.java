public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if ("/".equals(contextPath)) {
        // Invalid case where contextPath is "/"
        contextPath = ""; // Set contextPath to empty string
    }
    return decodeRequestString(request, contextPath);
}