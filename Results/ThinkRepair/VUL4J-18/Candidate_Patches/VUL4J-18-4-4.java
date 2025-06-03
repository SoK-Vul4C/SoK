public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request != null ? request.getPathInfo() : null;
    return pathInfo != null ? pathInfo : "";
}