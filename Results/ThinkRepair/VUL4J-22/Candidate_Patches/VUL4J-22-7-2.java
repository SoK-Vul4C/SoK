public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if ("/".equals(contextPath) || "".equals(contextPath)) {
        // Handle the case where the context path is either "/" or an empty string
        contextPath = "";  
    }
    return decodeRequestString(request, contextPath);
}