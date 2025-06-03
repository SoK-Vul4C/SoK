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

        if (!Objects.equals(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail()) ||
            !Objects.equals(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName()) ||
            scimUserFromDb.isVerified() != scimUserFromRequest.isVerified() ||
            scimUserFromDb.isActive() != scimUserFromRequest.isActive() ||
            !Objects.equals(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
            return false;
        }
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    return true;
}