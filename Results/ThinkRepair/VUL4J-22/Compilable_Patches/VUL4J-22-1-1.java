ide a fix for the buggy function

// Buggy Function
public static String getContextPath(HttpServletRequest request) {
    String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
    if (contextPath == null) {
        contextPath = request.getContextPath();
    }
    if ("/".equals(contextPath)) {
        contextPath = ""; // Fix: Change contextPath to empty string if it equals "/"
    }
    return decodeRequestString(request, contextPath);
}