private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
    throws ServletException, IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    if (resourceUrl == null) {
        // Handle the case where resourceUrl is null
        throw new ServletException("Resource URL is null");
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    for (String url : resourceUrls) {
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            throw new ServletException("Resource [" + url +
                    "] does not match allowed pattern [" + this.allowedResources + "]");
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Including resource [" + url + "]");
        }
        RequestDispatcher rd = request.getRequestDispatcher(url);
        if (rd == null) {
            throw new ServletException("Unable to get RequestDispatcher for URL: " + url);
        }
        rd.include(request, response);
    }
}