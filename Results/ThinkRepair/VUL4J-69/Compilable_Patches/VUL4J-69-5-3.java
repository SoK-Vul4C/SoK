private void doInclude(HttpServletRequest request, HttpServletResponse response, String resourceUrl)
		throws ServletException, IOException {

	if (this.contentType != null) {
		response.setContentType(this.contentType);
	}

	String[] resourceUrls = StringUtils.tokenizeToStringArray(resourceUrl, RESOURCE_URL_DELIMITERS);
	for (String url : resourceUrls) {
		// Check whether URL matches allowed resources
		if (this.allowedResources != null && !this.pathMatcher.match(this.allowedResources, url)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Set HTTP 403 Forbidden status
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Including resource [" + url + "]");
		}

		RequestDispatcher rd = request.getRequestDispatcher(url);
		rd.include(request, response);
	}
}