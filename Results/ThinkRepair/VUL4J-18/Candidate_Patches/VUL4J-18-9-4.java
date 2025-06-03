public String getForwardPage(HttpServletRequest request) {
    return request.getRequestURI(); // Corrected Line
}