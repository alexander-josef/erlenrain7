class UserController {

  def scaffold = User

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
        
        redirect(controller: 'user')
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
