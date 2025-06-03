public String getForwardPage(HttpServletRequest request) {
    StringBuilder forwardPage = new StringBuilder();
    
    if (request.getContextPath() != null) {
        forwardPage.append(request.getContextPath());
    }
    
    if (request.getServletPath() != null) {
        forwardPage.append(request.getServletPath());
    }
    
    if (request.getPathInfo() != null) {
        forwardPage.append(request.getPathInfo());
    }
    
    return forwardPage.toString();
}