class RoleController extends BaseController {

  def scaffold = Role

  def beforeInterceptor = [action: this.&auth]

}
