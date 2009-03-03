import ch.unartig.erlenrain7.grails.domain.Role
import ch.unartig.erlenrain7.grails.domain.User

public class ApplicationBootStrap {




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
        if (!User.findByUserId(User.SUPER_ADMIN_USERNAME)) {
            def superAdminRole = Role.findByRoleName(Role.SUPER_ADMIN_ROLE)
            User user = new User(userId: User.SUPER_ADMIN_USERNAME,
                    firstName: "Doelf",
                    lastName: "Josef",
                    email: "aj@unartig.ch",
                    hashedPassword: "123456",
                    party: "Josef/Knellwolf Wil",
                    role: superAdminRole
            )

            user.save()
        }


      // create default super admin user:
      if (!User.findByUserId(User.GUEST_USERNAME)) {
          def guestRole = Role.findByRoleName(Role.GUEST_ROLE)
          User guestUser = new User(userId: User.GUEST_USERNAME,
                  firstName: "Alexnader",
                  lastName: "Josef",
                  email: "alexander.josef@unartig.ch",
                  hashedPassword: "leonard",
                  party: "Josef/Knellwolf Wil",
                  role: guestRole
          )

          guestUser.save()
      }


        def doelf = User.findByUserId(User.SUPER_ADMIN_USERNAME)

        println(doelf)
    }

    def destroy = {
    }
}