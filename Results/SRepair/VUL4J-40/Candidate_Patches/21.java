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

@Test
public void testIsAllowed() throws Exception {
    // Arrange
    // Create mock objects and set expectations
    HttpServletRequest request = mock(HttpServletRequest.class);
    String requestBody = "{\"primaryEmail\":\"test@test.com\",\"userName\":\"test\",\"verified\":true,\"active\":true,\"origin\":\"uaa\"}";
    ScimUser scimUserFromRequest = JsonUtils.readValue(requestBody, ScimUser.class);
    String id = "12345";
    String zoneId = "test-zone";
    ScimUser scimUserFromDb = mock(ScimUser.class);

    when(UaaUrlUtils.getRequestPath(request)).thenReturn("/users/12345");
    when(scimUserProvisioning.retrieve(id, zoneId)).thenReturn(scimUserFromDb);
    when(scimUserFromDb.getPrimaryEmail()).thenReturn("test@test.com");
    when(scimUserFromDb.getUserName()).thenReturn("test");
    when(scimUserFromDb.isVerified()).thenReturn(true);
    when(scimUserFromDb.isActive()).thenReturn(true);
    when(scimUserFromDb.getOrigin()).thenReturn("uaa");

    // Act
    // Call the method being tested
    boolean result = isAllowed(request);

    // Assert
    // Verify that the expected results were obtained
    assertTrue(result);
}
}
