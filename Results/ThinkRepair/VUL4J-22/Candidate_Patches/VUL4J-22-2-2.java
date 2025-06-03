public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    String updatedContextPath = contextPath;
    if ("/".equals(contextPath)) {
        // Invalid case, but happens for includes on Jetty: silently adapt it.
        updatedContextPath = "";
    }
    return decodeRequestString(request, updatedContextPath);
}