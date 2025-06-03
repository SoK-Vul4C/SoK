public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null || contextPath.isEmpty()) {
        contextPath = request.getContextPath();
    }
    return decodeRequestString(request, contextPath);
}