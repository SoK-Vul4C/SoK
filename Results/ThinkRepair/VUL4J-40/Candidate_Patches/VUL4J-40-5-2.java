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

    if (!isEqual(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())) {
        return false;
    }

    if (!isEqual(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName())) {
        return false;
    }

    if (scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()) {
        return false;
    }

    if (scimUserFromDb.isActive() != scimUserFromRequest.isActive()) {
        return false;
    }

    if (!isEqual(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;
}

private boolean isEqual(Object obj1, Object obj2) {
    return obj1 != null && obj2 != null && obj1.equals(obj2);
}