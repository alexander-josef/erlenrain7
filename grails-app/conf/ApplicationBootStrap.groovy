public class ApplicationBootStrap {

    public static final String SUPER_ADMIN = "doelf2000"
    def init = {servletContext ->

        // create all current roles
        final String SUPER_ADMIN_ROLE = 'superAdmin'
        final String ADMIN_ROLE = 'admin'
        final String GUEST_ROLE = 'guest'
        if (!Role.findByRoleName(SUPER_ADMIN_ROLE)) {
            new Role(roleName: SUPER_ADMIN_ROLE).save()
        }

        if (!Role.findByRoleName(ADMIN_ROLE)) {
            new Role(roleName: ADMIN_ROLE).save()
        }

        if (!Role.findByRoleName(GUEST_ROLE)) {
            new Role(roleName: GUEST_ROLE).save()
        }



        // create default super admin user:
        if (!User.findByUserId(SUPER_ADMIN)) {
            User user = new User(userId: SUPER_ADMIN,
                    firstName: "Doelf",
                    lastName: "Josef",
                    email: "aj@unartig.ch",
                    hashedPassword: "123456",
                    party: "Josef/Knellwolf Wil")

            println user

            def result = user.save()
            println result
        }
    }

    def destroy = {
    }
}