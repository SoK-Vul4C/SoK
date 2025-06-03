public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if ("".equals(contextPath)) { // Fix: Check for empty string
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        contextPath = "";
    }
    return decodeRequestString(request, contextPath);
}