public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if (contextPath != null && !contextPath.isEmpty() && contextPath.equals("/")) {
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        contextPath = ""; 
    }
    return decodeRequestString(request, contextPath);
}