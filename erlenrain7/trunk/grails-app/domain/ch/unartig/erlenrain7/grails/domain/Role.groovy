package ch.unartig.erlenrain7.grails.domain
class Role {
    public static final SUPER_ADMIN_ROLE = 'superAdmin'
    public static final String ADMIN_ROLE = 'admin'
    public static final String GUEST_ROLE = 'guest'
    

    String roleName
    String description

    static optionals = ["description"]

    def String toString() {"${this.roleName}"}
}
