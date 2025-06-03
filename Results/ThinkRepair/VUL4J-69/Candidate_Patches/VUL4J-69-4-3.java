private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
        throws ServletException, IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    for (String url : resourceUrls) {
        // Check whether URL matches allowed resources
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            throw new ServletException("Resource [" + url +
                    "] does not match allowed pattern [" + this.allowedResources + "]");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Including resource [" + url + "]");
        }

        RequestDispatcher rd = request.getRequestDispatcher(url);
        if (rd == null) {
            throw new ServletException("Could not get RequestDispatcher for [" + url + "]");
        }

        try {
            rd.include(request, response);
        } catch (ServletException e) { // Catch ServletException thrown during include
            throw e; // Rethrow ServletException
        }
    }
}