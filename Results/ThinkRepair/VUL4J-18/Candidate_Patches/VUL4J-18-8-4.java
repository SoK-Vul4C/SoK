public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    return (pathInfo != null && !pathInfo.trim().isEmpty()) ? pathInfo : request.getRequestURI();
}