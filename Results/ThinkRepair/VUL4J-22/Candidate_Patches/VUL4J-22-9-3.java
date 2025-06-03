public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || "/".equals(contextPath)) {
        contextPath = ""; // set it to an empty string
    }
    return decodeRequestString(request, contextPath);
}