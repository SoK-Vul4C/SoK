private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
        throws ServletException, IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    for (String url : resourceUrls) {
        // Check whether URL matches allowed resources
        if (this.allowedResources != null && this.pathMatcher.match(this.allowedResources, url)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Including resource [" + url + "]");
            }

            RequestDispatcher rd = request.getRequestDispatcher(url);
            if (rd == null) {
                logger.warn("RequestDispatcher for [" + url + "] not found, skipping inclusion.");
                continue;
            }

            rd.include(request, response);
        } else {
            logger.warn("Skipping resource [" + url + "] as it does not match the allowed pattern [" + this.allowedResources + "]");
        }
    }
}