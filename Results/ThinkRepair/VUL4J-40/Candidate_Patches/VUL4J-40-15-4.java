public class ScimUser {
    private String primaryEmail;
    private String userName;
    private boolean verified;
    private boolean active;
    private String origin;

    // Constructors, getters, and setters

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScimUser other = (ScimUser) obj;
        return verified == other.verified &&
                active == other.active &&
                Objects.equals(primaryEmail, other.primaryEmail) &&
                Objects.equals(userName, other.userName) &&
                Objects.equals(origin, other.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryEmail, userName, verified, active, origin);
    }
}