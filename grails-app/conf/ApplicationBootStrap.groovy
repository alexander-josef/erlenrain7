import ch.unartig.erlenrain7.grails.domain.Role
import ch.unartig.erlenrain7.grails.domain.User

public class ApplicationBootStrap {

    public static final String SUPER_ADMIN_USERNAME = "doelf2000"


    def init = {servletContext ->

        // create all current roles
        if (!Role.findByRoleName(Role.SUPER_ADMIN_ROLE)) {
            new Role(roleName: Role.SUPER_ADMIN_ROLE,description:"Super Administrator for the application").save()
        }

        if (!Role.findByRoleName(Role.ADMIN_ROLE)) {
            new Role(roleName: Role.ADMIN_ROLE,description:"Administrator Role for one or several appartments").save()
        }

        if (!Role.findByRoleName(Role.GUEST_ROLE)) {
            new Role(roleName: Role.GUEST_ROLE,description:"Guest role for one appartment").save()
        }



        // create default super admin user:
        if (!User.findByUserId(SUPER_ADMIN_USERNAME)) {
            def superAdminRole = Role.findByRoleName(Role.SUPER_ADMIN_ROLE)
            User user = new User(userId: SUPER_ADMIN_USERNAME,
                    firstName: "Doelf",
                    lastName: "Josef",
                    email: "aj@unartig.ch",
                    hashedPassword: "123456",
                    party: "Josef/Knellwolf Wil",
                    role: superAdminRole
            )

            user.save()
        }

        def doelf = User.findByUserId("doelf2000")

        println(doelf)
    }

    def destroy = {
    }
}