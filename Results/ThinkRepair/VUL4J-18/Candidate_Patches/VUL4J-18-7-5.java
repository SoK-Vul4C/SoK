public String getForwardPage(HttpServletRequest request) {
    if (request == null) {
        return "/default-page"; // Return a default path if request is null
    }

    String pathInfo = request.getPathInfo();
    if (pathInfo == null || pathInfo.isEmpty()) {
        return "/default-page"; // Return a default path if pathInfo is null or empty
    }

    return pathInfo;
}