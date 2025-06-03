public String getForwardPage(HttpServletRequest request) {
    if (request != null) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            return pathInfo;
        }
    }
    return "/default-page"; // Providing a default path if pathInfo is null or request is null
}