public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
        return pathInfo;
    } else {
        return "defaultPage"; // Provide a default value if path information is null
    }
}