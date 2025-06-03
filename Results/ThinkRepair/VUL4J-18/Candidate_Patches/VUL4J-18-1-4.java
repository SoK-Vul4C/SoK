public String getForwardPage(HttpServletRequest request) {
    return request.getContextPath() + request.getPathInfo();
}