private LdapContext getLdapContextUsingStartTls(LdapContextFactory ldapContextFactory, Object principal, Object credentials) throws NamingException {
    JndiLdapContextFactory jndiLdapContextFactory = (JndiLdapContextFactory) ldapContextFactory;
    Hashtable<String, Object> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, jndiLdapContextFactory.getContextFactoryClassName());
    env.put(Context.PROVIDER_URL, jndiLdapContextFactory.getUrl());

    LdapContext ctx = null;

    try {
        ctx = new InitialLdapContext(env, null);

        StartTlsRequest startTlsRequest = new StartTlsRequest();
        StartTlsResponse tls = (StartTlsResponse) ctx.extendedOperation(startTlsRequest);

        tls.negotiate();

        ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, jndiLdapContextFactory.getAuthenticationMechanism());
        ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, principal);
        ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, credentials);

        return ctx; // Move this line inside the try block
    } catch (IOException e) {
        // Close the context if exception occurred during TLS negotiation
        LdapUtils.closeContext(ctx);
        securityLog.error(withRealm("Failed to negotiate TLS connection with '%s': ", server(jndiLdapContextFactory), e));
        throw new CommunicationException(e.getMessage());
    } catch (NamingException ne) {
        // Close the context and rethrow the exception
        LdapUtils.closeContext(ctx);
        throw ne;
    } catch (Throwable t) {
        // Close the context and throw the exception
        LdapUtils.closeContext(ctx);
        securityLog.error(withRealm("Unexpected failure to negotiate TLS connection with '%s': ", server(jndiLdapContextFactory), t));
        throw t;
    }
}

// A utility method for closing LDAP context
private void closeContext(LdapContext ctx) {
    if (ctx != null) {
        try {
            ctx.close();
        } catch (NamingException e) {
            // Log or handle exception if needed
        }
    }
}