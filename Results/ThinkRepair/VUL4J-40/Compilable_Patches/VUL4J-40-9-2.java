public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();

    try {
        ScimUser scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);

        if (scimUserFromDb == null || !scimUserFromDb.equals(scimUserFromRequest)) {
            return false;
        }
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    return true;
}