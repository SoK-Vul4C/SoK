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

    if (!Objects.equals(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())) {
        return false;
    }

    if (!isStringEqual(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName())) {
        return false;
    }

    if (scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()) {
        return false;
    }

    if (scimUserFromDb.isActive() != scimUserFromRequest.isActive()) {
        return false;
    }

    if (!isStringEqual(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;
}

private boolean isStringEqual(String str1, String str2) {
    if (str1 == null && str2 == null) {
        return true; // Both are null, consider them equal
    }
    if (str1 == null || str2 == null) {
        return false; // One is null, not equal
    }
    return str1.equals(str2);
}