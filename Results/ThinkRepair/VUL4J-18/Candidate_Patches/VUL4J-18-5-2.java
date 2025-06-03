public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    
    if (pathInfo != null) {
        pathInfo = pathInfo.trim();
        if (pathInfo.length() > 0 && pathInfo.charAt(0) == '/') {
            pathInfo = pathInfo.substring(1);
        }
        if (pathInfo.length() > 0 && pathInfo.charAt(pathInfo.length() - 1) == '/') {
            pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
        }
        return pathInfo;
    } else {
        return "defaultPage"; // Return default value if path information is null
    }
}