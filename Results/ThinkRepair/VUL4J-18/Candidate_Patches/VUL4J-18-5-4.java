public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    
    if (pathInfo != null) {
        pathInfo = pathInfo.trim();
        // Trim leading slash
        pathInfo = pathInfo.replaceAll("^/+", "");
        // Trim trailing slash
        pathInfo = pathInfo.replaceAll("/+$", "");

        return pathInfo.isEmpty() ? "defaultPage" : pathInfo;
    } else {
        return "defaultPage"; // Return default value if path information is null
    }
}