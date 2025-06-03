public String getForwardPage(HttpServletRequest request) {
    if (request != null) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            return pathInfo;
        }
    }
    return ""; // Or handle the case when request is null or pathInfo is null
}