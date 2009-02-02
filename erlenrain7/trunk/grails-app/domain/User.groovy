class User {

  String userId
  String hashedPassword
  String firstName
  String lastName
  String email
//    String phone

  static constraints = {
    userId(minSize: 6, unique: true)
    firstName(blank: false)
    lastName(blank: false)
    email(email: true, blank: false)
    hashedPassword(minSize: 6)
//
  }

  def String toString() {"${this.firstName} ${this.lastName}"}
}
