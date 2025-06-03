package org.cloudfoundry.identity.uaa.security;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.identity.uaa.scim.ScimUser;
import org.cloudfoundry.identity.uaa.scim.ScimUserProvisioning;
import org.cloudfoundry.identity.uaa.scim.exception.ScimResourceNotFoundException;
import org.cloudfoundry.identity.uaa.util.JsonUtils;
import org.cloudfoundry.identity.uaa.util.UaaUrlUtils;
import org.cloudfoundry.identity.uaa.zone.IdentityZoneHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class ScimUserSelfUpdateAllowed {

    public static final int USER_ID_PATH_PARAMETER_INDEX = 1;
    private ScimUserProvisioning scimUserProvisioning;

    public ScimUserSelfUpdateAllowed(ScimUserProvisioning scimUserProvisioning) {
        this.scimUserProvisioning = scimUserProvisioning;
    }

public boolean isAllowed(HttpServletRequest request) throws IOException {
    String requestBody = IOUtils.toString(request.getReader());
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);

    String id = UaaUrlUtils.extractPathVariableFromUrl(USER_ID_PATH_PARAMETER_INDEX, UaaUrlUtils.getRequestPath(request));
    String zoneId = IdentityZoneHolder.get().getId();
    ScimUser scimUserFromDb;

    try {
        scimUserFromDb = scimUserProvisioning.retrieve(id, zoneId);
    } catch (ScimResourceNotFoundException e) {
        return false;
    }

    if (!compareUsers(scimUserFromDb, scimUserFromRequest)) {
        return false;
    }

    return true;
}

private boolean compareUsers(ScimUser user1, ScimUser user2) {
    return user1.getPrimaryEmail().equals(user2.getPrimaryEmail())
            && user1.getUserName().equals(user2.getUserName())
            && user1.isVerified() == user2.isVerified()
            && user1.isActive() == user2.isActive()
            && user1.getOrigin().equals(user2.getOrigin());
}
}
