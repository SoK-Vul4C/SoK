public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
        return pathInfo;
    } else {
        return "/defaultPage"; // Provide an appropriate default page path
    }
}