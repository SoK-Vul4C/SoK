public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || "/".equals(contextPath)) {
        // Invalid case: contextPath is null or "/"
        contextPath = ""; // Set contextPath to empty string
    }
    return decodeRequestString(request, contextPath);
}