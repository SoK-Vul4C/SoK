public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);
    
    // Check if the request is null
    if (scimUserFromRequest == null) {
        return false;
    }

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    // Check if the DB user is null
    if (scimUserFromDb == null) {
        return false;
    }

    // Check the individual properties for null before comparing
    if (!Objects.equals(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())) {
        return false;
    }

    if (!Objects.equals(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName())) {
        return false;
    }

    if (scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()) {
        return false;
    }

    if (scimUserFromDb.isActive() != scimUserFromRequest.isActive()) {
        return false;
    }

    if (!Objects.equals(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;
}