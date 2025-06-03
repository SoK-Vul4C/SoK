public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if (contextPath != null && "/".equals(contextPath)) { // Fixed Line
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        contextPath = ""; // Fixed Line
    }
    return decodeRequestString(request, contextPath);
}