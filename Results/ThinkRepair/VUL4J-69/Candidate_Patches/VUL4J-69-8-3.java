private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
	throws IOException {

	if (this.contentType != null) {
		response.setContentType(this.contentType);
	}

	String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
	for (String url : resourceUrls) {
		try {
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
			if (rd != null) {
				rd.include(request, response);
			} else {
				logger.warn("Cannot get RequestDispatcher for URL: " + url);
			}
		} catch (Exception e) {
			logger.error("Error including resource [" + url + "]: " + e.getMessage());
		}
	}
}