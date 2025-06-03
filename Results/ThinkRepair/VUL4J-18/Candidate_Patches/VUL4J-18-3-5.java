public String getForwardPage(HttpServletRequest request) {
    if (request != null) { // Null check for request object
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            return pathInfo;
        }
    }
    return "DefaultPage"; // Default value when pathInfo is null, empty, or request is null
}