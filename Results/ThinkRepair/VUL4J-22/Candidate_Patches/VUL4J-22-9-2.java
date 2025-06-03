public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || "/".equals(contextPath)) {
        // Invalid case or includes on Jetty: set it to the context path of the request
        contextPath = request.getContextPath();
    }
    return decodeRequestString(request, contextPath);
}