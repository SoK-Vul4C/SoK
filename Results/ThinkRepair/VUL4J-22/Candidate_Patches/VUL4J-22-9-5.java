public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || "/".equals(contextPath)) {
        contextPath = "";
    }
    // Handle decoding of context path (replace decodeRequestString with actual decoding method)
    // return decodeRequestString(request, contextPath);
    return contextPath; // Placeholder for context path handling
}