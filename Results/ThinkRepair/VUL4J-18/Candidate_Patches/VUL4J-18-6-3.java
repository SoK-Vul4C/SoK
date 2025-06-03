public String getForwardPage(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String servletPath = request.getServletPath();
    String pathInfo = request.getPathInfo();
    
    // Determine the forward page by combining context path, servlet path, and path info
    String forwardPage = contextPath + servletPath;
    if (pathInfo != null) {
        forwardPage += pathInfo;
    }
    
    return forwardPage;
}