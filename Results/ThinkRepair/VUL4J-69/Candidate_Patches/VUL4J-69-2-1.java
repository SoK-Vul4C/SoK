private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
        throws ServletException, IOException {

    if (this.contentType != null) {
        response.setContentType(this.contentType);
    }

    String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
    boolean allResourcesMatch = true;

    for (String url : resourceUrls) {
        // Check whether URL matches allowed resources
        if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
            allResourcesMatch = false;
            break;
        }
    }

    if (!allResourcesMatch) {
        // At least one resource does not match allowed pattern
        throw new ServletException("Some resources do not match the allowed pattern: " + Arrays.toString(resourceUrls));
    }

    // All resources match, include them
    for (String url : resourceUrls) {
        if (logger.isDebugEnabled()) {
            logger.debug("Including resource [" + url + "]");
        }
        RequestDispatcher rd = request.getRequestDispatcher(url);
        rd.include(request, response);
    }
}