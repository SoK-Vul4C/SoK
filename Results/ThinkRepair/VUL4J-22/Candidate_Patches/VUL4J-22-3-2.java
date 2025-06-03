public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || "/".equals(contextPath)) { // Fix: Check for null or "/"
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        return decodeRequestString(request, ""); // Return empty string
    }
    return decodeRequestString(request, contextPath);
}