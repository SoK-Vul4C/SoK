public String getForwardPage(HttpServletRequest request) {
    String contextPath = request.getContextPath();
    String servletPath = request.getServletPath();
    
    // Determine the forward page by combining context path and servlet path
    String forwardPage = contextPath + servletPath;
    
    return forwardPage;
}