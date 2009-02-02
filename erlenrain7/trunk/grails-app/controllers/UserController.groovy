class UserController extends BaseController {

  def scaffold = User


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
    redirect(controller: 'race', action: 'search')
  }
}
