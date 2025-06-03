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

public void testIsAllowed() throws IOException {
    // Arrange
    String id = "123456";
    String zoneId = "default";
    ScimUser scimUserFromDb = new ScimUser("user@example.com", "user", true, true, "origin");

    when(scimUserProvisioning.retrieve(id, zoneId)).thenReturn(scimUserFromDb);
    when(UaaUrlUtils.getRequestPath(request)).thenReturn("/users/" + id);
    when(IdentityZoneHolder.get().getId()).thenReturn(zoneId);

    // Act
    boolean allowed = isAllowed(request);

    // Assert
    verify(scimUserProvisioning).retrieve(id, zoneId);
    verify(UaaUrlUtils).getRequestPath(request);
    verify(IdentityZoneHolder).get();

    assertTrue(allowed);
}
}
