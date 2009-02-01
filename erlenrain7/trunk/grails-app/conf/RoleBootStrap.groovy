public class RoleBootStrap {

    def init = {
        final String SUPER_ADMIN_ROLE = 'superAdmin'
        if (!Role.findByName(SUPER_ADMIN_ROLE)) {
            new Role(name: SUPER_ADMIN_ROLE).save()
        }
    }

    def destroy = {

    }
}