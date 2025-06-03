public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return true;  // User not found means allowed
    }

    // Compare primary email
    if (!Objects.equals(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())) {
        return false;
    }

    // Compare userName with null checks
    if (scimUserFromDb.getUserName() == null || scimUserFromRequest.getUserName() == null ||
            !scimUserFromDb.getUserName().equals(scimUserFromRequest.getUserName())) {
        return false;
    }

    // Compare verified status
    if (scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()) {
        return false;
    }

    // Compare active status
    if (scimUserFromDb.isActive() != scimUserFromRequest.isActive()) {
        return false;
    }

    // Compare origin
    if (!Objects.equals(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;  // All checks passed, user is allowed
}