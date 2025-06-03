public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
        return pathInfo;
    } else {
        return "DefaultPage"; // Default value when pathInfo is null
    }
}