public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath != null && !contextPath.equals("/")) {
        return decodeRequestString(request, contextPath);
    } else {
        return request.getContextPath();
    }
}