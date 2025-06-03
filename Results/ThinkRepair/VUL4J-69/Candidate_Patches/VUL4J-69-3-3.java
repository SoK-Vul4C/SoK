private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
        throws IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    for (String url : resourceUrls) {
        boolean isAllowed = true;

        // Check whether URL matches allowed resources
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            isAllowed = false;
            if (logger.isDebugEnabled()) {
                logger.debug("Resource [" + url + "] does not match allowed pattern [" + this.allowedResources + "], skipping include.");
            }
        }

        if (isAllowed) {
            if (logger.isDebugEnabled()) {
                logger.debug("Including resource [" + url + "]");
            }

            try {
                RequestDispatcher rd = request.getRequestDispatcher(url);
                rd.include(request, response);
            } catch (ServletException e) {
                logger.error("Error including resource [" + url + "]", e);
            }
        }
    }
}