public String getForwardPage(HttpServletRequest request) {
    String forwardPage = request.getParameter("forwardPage");
    
    if (forwardPage == null) {
        forwardPage = (String) request.getAttribute("forwardPage");
    }
    
    if (forwardPage == null) {
        forwardPage = "defaultForwardPage.html"; // Default forward page
    }
    
    return forwardPage;
}