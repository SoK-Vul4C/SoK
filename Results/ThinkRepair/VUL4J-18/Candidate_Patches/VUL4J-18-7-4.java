public String getForwardPage(HttpServletRequest request) {
    if (request != null) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            return pathInfo;
        } else {
            return "/default-page"; // Return a default path if pathInfo is null
        }
    } else {
        return "/default-page"; // Return a default path if request is null
    }
}