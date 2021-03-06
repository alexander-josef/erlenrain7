package ch.unartig.erlenrain7.grails.domain

import ch.unartig.erlenrain7.grails.domain.Role

/**
 * Also used as 'Guest' throughout the application. A user belongs to one (standard case) or many appartments.
 */
class User {

  // custom mapping for user since postgresql cannot use 'user' as table name!
  static mapping = {
        table 'user_table'
  }


  static String SUPER_ADMIN_USERNAME = "doelf2000"
  static String GUEST_USERNAME = "alexander.josef"

  String userId
  String hashedPassword
  String firstName
  String lastName
  String email
  String party // a user belongs to a party/familiy
//  String phone
  Role role
  // a user can have one (standard case) or many appartments
  List<Wohnung> appartments

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
