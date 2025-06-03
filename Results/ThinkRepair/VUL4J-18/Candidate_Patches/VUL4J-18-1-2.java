public String getForwardPage(HttpServletRequest request) {
    String path = request.getServletPath() + request.getPathInfo();
    return path;
}