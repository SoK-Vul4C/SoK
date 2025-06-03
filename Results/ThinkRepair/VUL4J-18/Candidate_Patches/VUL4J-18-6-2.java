public String getForwardPage(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String servletPath = request.getServletPath();
    String pathInfo = request.getPathInfo();
    
    // Concatenate context path, servlet path, and path info to get the complete forward page
    return contextPath + servletPath + (pathInfo != null ? pathInfo : "");
}