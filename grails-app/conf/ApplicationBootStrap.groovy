public class ApplicationBootStrap {

  def init = { servletContext ->
    final String SUPER_ADMIN = "doelf2000"
    if (!User.findByUserId(SUPER_ADMIN)) {
//      User user = new User(userId: SUPER_ADMIN, hashedPassword: "leonard", firstName: "Doelf", lastName: "Josef", email: "doelf@erlenrain7.ch")
      User user = new User(userId: SUPER_ADMIN)

      println user

      def result = user.save()
      println result
    }
  }

  def destroy = {
  }
}