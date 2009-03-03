import ch.unartig.erlenrain7.domain.Reservation
import ch.unartig.erlenrain7.grails.domain.User
import ch.unartig.erlenrain7.util.GDataHelper
import com.google.gdata.data.extensions.EventEntry
import javax.mail.Session
import javax.mail.Message
import javax.mail.internet.MimeMessage
import javax.mail.internet.InternetAddress
import java.text.SimpleDateFormat
import ch.unartig.erlenrain7.grails.domain.Wohnung
import ch.unartig.erlenrain7.util.Erlenrain7Util


class WohnungController extends BaseController {

  def scaffold = Wohnung

  /**
   * Todo Caching?
   */
  def getGuest = {
    User.findByUserId(session.userId)
  }


  def beforeInterceptor = [action: this.&auth]


  def reservation
  def listOfUnconfirmedReservations

  /**
   * read the calendar entries and also put the active user in the request scope
   */
  def index = {
    readCalendarEntries()
    [guest: getGuest(), listOfUnconfirmedReservations: listOfUnconfirmedReservations]
  }




  /**
   * update an entry from tentative to confirmed or delete if reservation declined
   */
  def update = {
    String reservationId = params.googleHref
    EventEntry entry = GDataHelper.updateReservationEventEntry(reservationId)

    // startTime in Millis
    long startTime = entry.times[0].startTime.value
    // endTime in Millis
    long endTime = entry.times[0].endTime.value
    Erlenrain7Util.sendConfirmationEmailFor(params.guestEmail, new Date(startTime), new Date(endTime))
    flash['message'] = 'Reservation wurde bestätigt -- Eine Bestätigungs-E-Mail wurde versandt.'
    redirect(action: index)
  }

  def deleteReservation = {

    // todo
  }



  /**
   * Process the input (start and end date) and call the reservation object to create a tentative google calendar entry.
   */
  def reserve = {
    println "... making reservation"

    String startDateInput = "${params.reservationStartDate_year}/${params.reservationStartDate_month}/${params.reservationStartDate_day}"
    String endDateInput = "${params.reservationEndDate_year}/${params.reservationEndDate_month}/${params.reservationEndDate_day}"
    Date startDate = new SimpleDateFormat("yyyy/MM/dd").parse(startDateInput)
    Date endDate = new SimpleDateFormat("yyyy/MM/dd").parse(endDateInput)

    // make entry to calendar with status tentativly

    // todo add wohnung to reservation from session info wohnungid
    reservation = new Reservation(wohnung: Wohnung.get(1), guest: getGuest(), startDate: startDate, endDate: endDate)

    reservation.reserveTentatively()
    // making a reservation sets a success code and - if they exist - existing reservations for that period
    if (!reservation.success){
      String occupiedMessage = "Reservation nicht moeglich, folgende Reservationen liegen bereits vor:  </br> \n"
      reservation.existingEvents.each {entry -> occupiedMessage = occupiedMessage + entry.getTitle().getPlainText() + " -- " + new Date(entry.times[0].startTime.value) + " bis " + new Date(entry.times[0].endTime.value) + "</br>\n"}
      flash['message'] = occupiedMessage
      redirect(action: index)
    }
    reservation.sendEmailToWohnungAdministrator(request)

    // return success message    
    flash['message'] = 'Reservation wurde eingetragen. Du erhältst in Kürze ein Bestätigungs-E-Mail.'

    redirect(action: index)


  }

  /**
   * My first groovy and GDATA code
   * What does it do ?
   * It currently reads the entries from the separate google account "erlenrain7@unartig.ch"
   *
   */
  void readCalendarEntries() {
    def logggedInGuest = getGuest()
    listOfUnconfirmedReservations = GDataHelper.readUnconfirmedEntries(logggedInGuest)
  }











}
