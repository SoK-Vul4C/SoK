public String getForwardPage(HttpServletRequest request) {
    String forwardPage = request.getParameter("forwardPage");
    
    if (forwardPage == null) {
        forwardPage = (String) request.getAttribute("forwardPage");
    }
    
    return forwardPage;
}