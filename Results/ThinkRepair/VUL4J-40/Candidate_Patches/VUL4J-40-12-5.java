public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    if (scimUserFromRequest == null) {
        return false; // Check if the request could not be parsed into a ScimUser object
    }

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb = null;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return true; // If user not found, return true
    } catch (Exception e) {
        return false; // If an exception occurs while retrieving the user, return false
    }

    if (scimUserFromDb == null) {
        return false; // Check if the user from the database is null
    }

    // Compare the properties of the objects with null checks
    if (Objects.equals(scimUserFromDb.getPrimaryEmail(), scimUserFromRequest.getPrimaryEmail())
            && Objects.equals(scimUserFromDb.getUserName(), scimUserFromRequest.getUserName())
            && scimUserFromDb.isVerified() == scimUserFromRequest.isVerified()
            && scimUserFromDb.isActive() == scimUserFromRequest.isActive()
            && Objects.equals(scimUserFromDb.getOrigin(), scimUserFromRequest.getOrigin())) {
        return true; // All properties match, return true
    }

    return false; // If any property does not match, return false
}