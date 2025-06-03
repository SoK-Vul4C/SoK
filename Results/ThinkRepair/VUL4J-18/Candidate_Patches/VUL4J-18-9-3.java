public String getForwardPage(HttpServletRequest request) {
    String servletPath = request.getServletPath();
    String pathInfo = request.getPathInfo();
    
    if (pathInfo != null) {
        return servletPath + pathInfo; // Corrected Line
    } else {
        return servletPath; // Return just the servlet path if pathInfo is null
    }
}