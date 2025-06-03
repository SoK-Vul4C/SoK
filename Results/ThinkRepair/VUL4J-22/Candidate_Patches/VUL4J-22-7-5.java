public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }

    if ("/".equals(contextPath)) {
        // Adjust contextPath to an empty string when it is "/"
        contextPath = "";
    } else if ("".equals(contextPath)) {
        // Adjust contextPath to "/" when it is an empty string
        contextPath = "/";
    }

    return decodeRequestString(request, contextPath);
}