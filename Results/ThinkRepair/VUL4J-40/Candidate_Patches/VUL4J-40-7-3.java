public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);

        if (scimUserFromDb == null || scimUserFromRequest == null) {
            return false;
        }

        // Check if the fields are null before comparing
        if ((scimUserFromDb.getPrimaryEmail() == null || scimUserFromRequest.getPrimaryEmail() == null) ||
                !scimUserFromDb.getPrimaryEmail().equals(scimUserFromRequest.getPrimaryEmail()) ||
            (scimUserFromDb.getUserName() == null || scimUserFromRequest.getUserName() == null) ||
                !scimUserFromDb.getUserName().equals(scimUserFromRequest.getUserName()) ||
                scimUserFromDb.isVerified() != scimUserFromRequest.isVerified() ||
                scimUserFromDb.isActive() != scimUserFromRequest.isActive() ||
            (scimUserFromDb.getOrigin() == null || scimUserFromRequest.getOrigin() == null) ||
                !scimUserFromDb.getOrigin().equals(scimUserFromRequest.getOrigin())) {
            return false;
        }
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    return true;
}