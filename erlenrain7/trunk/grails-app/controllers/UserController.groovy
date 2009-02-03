class UserController {


    /**
     * Intercept all reqeusts with a login page, except the login and logout action
     */
    def beforeInterceptor = [action: this.&auth,
            except: ['login', 'logout']]

    /**
     * Login closure:
     */
    def login = {
        if (request.method == "GET") {
            session.userId = null
            def user = new User()
        }
        else if (request.method == "POST") {
            def user = User.findByUserIdAndHashedPassword(params.userId, params.password)
            if (user) {
                session.userId = user.userId
                println("succesfully logged in for user ${user}!")

                // forward to original action and params
                def redirectParams =
                session.originalRequestParams ?
                    session.originalRequestParams :
                    [controller: 'wohnung']
                redirect(redirectParams)

            }
            else {
                flash['message'] = 'Please enter a valid user ID and password'
            }
        }
        else {
            flash['message'] = 'Unknown method!'

        }
    }


    def logout = {
        session.userId = null
        flash['message'] = 'Successfully logged out'
        redirect(controller: 'wohnung', action: 'index')
    }

    // Start scaffolding:

    def index = { redirect(action: list, params: params) }

    // the delete, save and update actions only accept POST requests
    def allowedMethods = [delete: 'POST', save: 'POST', update: 'POST']



    def list = {
        if (!params.max) params.max = 10
        [userInstanceList: User.list(params)]
    }

    def show = {
        def userInstance = User.get(params.id)

        if (!userInstance) {
            flash.message = "User not found with id ${params.id}"
            redirect(action: list)
        }
        else { return [userInstance: userInstance] }
    }

    def delete = {
        def userInstance = User.get(params.id)
        if (userInstance) {
            userInstance.delete()
            flash.message = "User ${params.id} deleted"
            redirect(action: list)
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action: list)
        }
    }

    def edit = {
        def userInstance = User.get(params.id)

        if (!userInstance) {
            flash.message = "User not found with id ${params.id}"
            redirect(action: list)
        }
        else {
            return [userInstance: userInstance]
        }
    }

    def update = {
        def userInstance = User.get(params.id)
        if (userInstance) {
            userInstance.properties = params
            if (!userInstance.hasErrors() && userInstance.save()) {
                flash.message = "User ${params.id} updated"
                redirect(action: show, id: userInstance.id)
            }
            else {
                render(view: 'edit', model: [userInstance: userInstance])
            }
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action: edit, id: params.id)
        }
    }

    def create = {
        def userInstance = new User()
        userInstance.properties = params
        return ['userInstance': userInstance]
    }

    def save = {
        def userInstance = new User(params)
        if (!userInstance.hasErrors() && userInstance.save()) {
            flash.message = "User ${userInstance.id} created"
            redirect(action: show, id: userInstance.id)
        }
        else {
            render(view: 'create', model: [userInstance: userInstance])
        }
    }
}
