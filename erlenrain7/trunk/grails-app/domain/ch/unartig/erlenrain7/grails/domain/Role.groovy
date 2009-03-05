package ch.unartig.erlenrain7.grails.domain
class Role {


// custom mapping for role since postgresql cannot use 'role' as table name!
  static mapping = {
    table 'user_role'
  }

  public static final SUPER_ADMIN_ROLE = 'superAdmin'
  public static final String ADMIN_ROLE = 'admin'
  public static final String GUEST_ROLE = 'guest'


  String roleName
  String description

  static optionals = ["description"]

  def String toString() {"${this.roleName}"}
}
