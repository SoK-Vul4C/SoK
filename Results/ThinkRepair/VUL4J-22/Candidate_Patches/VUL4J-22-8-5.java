public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);

    if (contextPath == null) {
        contextPath = request.getContextPath();
    }

    if ("/".equals(contextPath)) {
        // Handle the case where contextPath is set to "/"
        // Return contextPath as is
        return contextPath;
    }

    return decodeRequestString(request, contextPath);
}