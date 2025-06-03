public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    
    if (pathInfo != null) {
        pathInfo = pathInfo.trim();
        if (pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        if (pathInfo.endsWith("/")) {
            pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
        }
        return pathInfo.isEmpty() ? "defaultPage" : pathInfo;
    } else {
        return "defaultPage"; // Return default value if path information is null
    }
}