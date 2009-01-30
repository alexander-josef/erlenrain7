/**
 * Created by IntelliJ IDEA.
 * User: alexanderjosef
 * Date: Jan 30, 2009
 * Time: 6:33:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class Guest {
    String firstName
    String lastName
    String email
    String phone

    def String toString() {"${this.firstName}  ${this.lastName}"}
}