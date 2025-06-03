public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    
    if ("/".equals(contextPath)) {
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        contextPath = ""; // Update contextPath to an empty string
    }
    
    return contextPath; // Return the correctly handled contextPath
}