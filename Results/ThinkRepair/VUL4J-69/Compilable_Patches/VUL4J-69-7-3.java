private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
	throws ServletException, IOException 
{
	if (this.contentType != null) {
		response.setContentType(this.contentType);
	}

	String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
	for (String url : resourceUrls) {
		if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
			logger.error("Resource [" + url + "] does not match allowed pattern [" + this.allowedResources + "]");
			// Skip the current resource URL and continue to the next one
			continue;
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Including resource [" + url + "]");
			}
			RequestDispatcher rd = request.getRequestDispatcher(url);
			rd.include(request, response);
		}
	}
}