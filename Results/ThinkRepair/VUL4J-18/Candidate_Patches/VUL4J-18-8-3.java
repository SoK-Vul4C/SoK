public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && !pathInfo.trim().isEmpty()) {
        return pathInfo;
    } else {
        return request.getRequestURI(); // Returning the request URI as a fallback
    }
}