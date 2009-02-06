class User {

  String userId
  String hashedPassword
  String firstName
  String lastName
  String email
  String party // a user belongs to a party/familiy
//  String phone
  Role role  

  static constraints = {
    userId(minSize: 6, unique: true)
    firstName(blank: false)
    lastName(blank: false)
    email(email: true, blank: false)
    hashedPassword(minSize: 6)
    role(blank:false)
  }
    
//    static optionals = ["phone"]

    boolean userIsAdmin(){
        return role.equals(Role.findByRoleName(Role.SUPER_ADMIN_ROLE))
    }

    

  def String toString() {"${this.firstName} ${this.lastName}"}
}
