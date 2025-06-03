public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && !pathInfo.isEmpty()) {
        return pathInfo;
    } else {
        // Handle the case where PathInfo is null or empty
        return "defaultPage";
    }
}