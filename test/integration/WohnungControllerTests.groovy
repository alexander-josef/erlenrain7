import com.google.gdata.client.calendar.CalendarService

class WohnungControllerTests extends GroovyTestCase {
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"

    def myService = new CalendarService("codehausGroovy-groovyExampleApp-1")



    void testSomething() {

    }

    void testUserCredentials() {
        myService.setUserCredentials(myId,myPassword);
    }
}
