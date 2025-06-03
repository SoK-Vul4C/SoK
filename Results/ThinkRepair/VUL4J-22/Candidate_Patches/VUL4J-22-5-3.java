public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    } else if ("/".equals(contextPath)) {
        contextPath = ""; // Set contextPath to empty string if it is "/"
    }
    return decodeRequestString(request, contextPath);
}