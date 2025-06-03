private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
        throws ServletException, IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    for (String url : resourceUrls) {
        // Check whether URL matches allowed resources
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            String errorMessage = "Resource [" + url + "] does not match allowed pattern [" + this.allowedResources + "]";
            logger.error(errorMessage);
            throw new ServletException(errorMessage);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Including resource [" + url + "]");
        }
        RequestDispatcher rd = request.getRequestDispatcher(url);
        if (rd == null) {
            logger.error("RequestDispatcher is null for resource [" + url + "]");
            throw new ServletException("RequestDispatcher is null for resource [" + url + "]");
        }
        rd.include(request, response);
    }
}