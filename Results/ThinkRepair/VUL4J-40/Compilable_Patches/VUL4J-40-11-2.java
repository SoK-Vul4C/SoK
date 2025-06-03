public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    // Check if the primary email, username, and other relevant properties match
    if (!scimUserFromDb.getPrimaryEmail().equals(scimUserFromRequest.getPrimaryEmail())
            || !scimUserFromDb.getUserName().equals(scimUserFromRequest.getUserName())
            || scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()
            || scimUserFromDb.isActive() != scimUserFromRequest.isActive()
            || !scimUserFromDb.getOrigin().equals(scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;
}