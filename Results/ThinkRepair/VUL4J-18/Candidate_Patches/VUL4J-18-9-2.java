public String getForwardPage(HttpServletRequest request) {
    return request.getServletPath(); // Corrected Line
}