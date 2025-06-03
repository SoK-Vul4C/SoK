public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && !pathInfo.isBlank()) {
        return pathInfo;
    } else {
        // Handle the case where PathInfo is null, empty, or contains only whitespace
        return "defaultPage";
    }
}