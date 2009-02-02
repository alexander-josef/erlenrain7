public class ApplicationBootStrap {

    public static final String SUPER_ADMIN = "doelf2000"
    def init = {servletContext ->
        if (!User.findByUserId(SUPER_ADMIN)) {
//      User user = new User(userId: SUPER_ADMIN, hashedPassword: "leonard", firstName: "Doelf", lastName: "Josef", email: "doelf@erlenrain7.ch")
            User user = new User(userId: SUPER_ADMIN, firstName: "Doelf", lastName: "Josef", email: "doelf@bluewin.ch", hashedPassword: "123456")

            println user

            def result = user.save()
            println result
        }
    }

    def destroy = {
    }
}