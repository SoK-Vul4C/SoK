public String getForwardPage(HttpServletRequest request) {
    String pathInfo = request.getPathInfo();
    if (pathInfo != null) {
        return pathInfo;
    } else {
        // Handle the case where PathInfo is null, return a default page, or perform additional logic
        return "defaultPage";
    }
}