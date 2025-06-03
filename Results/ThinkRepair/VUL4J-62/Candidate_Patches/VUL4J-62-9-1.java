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

        // Check if the context setup was successful before returning
        if (ctx != null) {
            return ctx;
        } else {
            throw new CommunicationException("Failed to establish LDAP connection with TLS negotiation");
        }
    } catch (IOException e) {
        LdapUtils.closeContext(ctx);
        securityLog.error(withRealm("Failed to negotiate TLS connection with '%s': ", server(jndiLdapContextFactory), e));
        throw new CommunicationException(e.getMessage());
    } catch (Throwable t) {
        LdapUtils.closeContext(ctx);
        securityLog.error(withRealm("Unexpected failure to negotiate TLS connection with '%s': ", server(jndiLdapContextFactory), t));
        throw t;
    }
}