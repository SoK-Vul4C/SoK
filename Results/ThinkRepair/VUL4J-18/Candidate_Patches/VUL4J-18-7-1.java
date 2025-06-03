public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
        return pathInfo; // Fixed Line
    } else {
        return ""; // Or handle the null case as needed
    }
}