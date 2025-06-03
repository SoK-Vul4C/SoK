public String getForwardPage(HttpServletRequest request) {
    if (request != null) { // Null check for request object
        String pathInfo = request.getRequestURI();
        if (pathInfo != null) {
            return pathInfo;
        }
    }
    return "DefaultPage"; // Default value if request or URI is null
}