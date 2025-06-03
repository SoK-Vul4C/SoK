private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
	throws ServletException, IOException {

	if (this.contentType != null) {
		response.setContentType(this.contentType);
	}

	String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
	for (String url : resourceUrls) {
		// Check whether URL matches allowed resources
		if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Skipping resource [" + url + "] as it does not match allowed pattern [" + this.allowedResources + "]");
			}
			continue; // Skip including the resource and move to the next URL
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Including resource [" + url + "]");
		}
		RequestDispatcher rd = request.getRequestDispatcher(url);
		if (rd == null) {
			logger.warn("Cannot get RequestDispatcher for URL: " + url);
			continue; // Skip including the resource if the RequestDispatcher is null
		}
		rd.include(request, response);
	}
}