public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    if (scimUserFromRequest == null) {
        return false;
    }

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb = null;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return true;
    }

    if (scimUserFromDb == null) {
        return false;
    }

    // Check properties individually for null
    if (!compareStrings(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())
            || !compareStrings(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName())
            || scimUserFromDb.isVerified() != scimUserFromRequest.isVerified()
            || scimUserFromDb.isActive() != scimUserFromRequest.isActive()
            || !compareStrings(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return false;
    }

    return true;
}

private boolean compareStrings(String str1, String str2) {
    if (str1 == null && str2 == null) {
        return true;
    }
    return str1 != null && str1.equals(str2);
}