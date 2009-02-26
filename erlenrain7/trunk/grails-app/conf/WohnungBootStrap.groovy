import ch.unartig.erlenrain7.grails.domain.User
import ch.unartig.erlenrain7.grails.domain.Wohnung

public class WohnungBootStrap {
  
  def init = {servletContext ->

    // create default wohnung only if no Wohnung exists:
    if (Wohnung.list().empty) {

      Wohnung wohnung = new Wohnung(
              appartmentName: "Erlenrain 7",
              city: "Engelberg",
              country: "CH",
              zipCode: 6390,
              address1: "Engelbergerstrasse 57",
              administrator: User.findByUserId(User.SUPER_ADMIN_USERNAME)
      )

      def result = wohnung.save()

      println result
      
      println(wohnung) 
    }

//    def allAppartments = Wohnung.list()
//
//    allAppartments.each {it ->
//      println(it.appartmentName)
//    }
  }

  def destroy = {

  }
}