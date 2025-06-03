public String getForwardPage(HttpServletRequest request) {
    if (request != null) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            return pathInfo;
        }
    }
    return "";
}