private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl) 
        throws ServletException, IOException {
    
    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }
    
    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);

    for (String url : resourceUrls) {
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            throw new ServletException("Resource [" + url + "] does not match the allowed pattern [" + this.allowedResources + "]");
        }
    }

    for (String url : resourceUrls) {
        if (logger.isDebugEnabled()) {
            logger.debug("Including resource [" + url + "]");
        }
        RequestDispatcher rd = request.getRequestDispatcher(url);
        try {
            rd.include(request, response);
        } catch (IOException | ServletException e) {
            // Handle any exceptions thrown during RequestDispatcher include
            throw new ServletException("Error including resource [" + url + "]", e);
        }
    }
}