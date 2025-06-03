public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null && !pathInfo.isEmpty()) {
        return pathInfo;
    } else {
        return "defaultPage"; // Default forward page
    }
}