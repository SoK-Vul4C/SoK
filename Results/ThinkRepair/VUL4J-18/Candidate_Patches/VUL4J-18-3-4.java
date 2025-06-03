public String getForwardPage(HttpServletRequest request) {
    if (request != null) { // Null check for request object
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            return pathInfo;
        }
    }
    return "DefaultPage"; // Default value when pathInfo is null or request is null
}