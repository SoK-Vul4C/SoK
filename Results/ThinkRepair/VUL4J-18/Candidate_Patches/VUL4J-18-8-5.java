public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && !pathInfo.isBlank()) {
        return pathInfo;
    } else {
        return request.getRequestURI(); // Fallback to request URI
    }
}