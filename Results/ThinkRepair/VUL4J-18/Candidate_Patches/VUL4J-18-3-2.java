public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getRequestURI();
    if (pathInfo != null) {
        return pathInfo;
    } else {
        return "DefaultPage"; // Default value when pathInfo is null
    }
}