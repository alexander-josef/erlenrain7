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


class WohnungController extends BaseController {

  def scaffold = Wohnung

  /**
   * Todo Caching?
   */
  def getGuest = {
    User.findByUserId(session.userId)
  }

  /**
   *
   */
  def getAdministrator = {
    // Todo Administrator shall be loaded by Guest --> Wohnung --> Admin
    User.findByUserId(ApplicationBootStrap.SUPER_ADMIN_USERNAME)
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
   * URL: http://localhost:8080/erlenrain7/wohnung/show
   * This closure (?) is called for the show operation, see above URL
   */
  def show = {
    readCalendarEntries()
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
    sendConfirmationEmailFor(params.guestEmail, new Date(startTime), new Date(endTime))
    flash['message'] = 'Reservation wurde erfolgreich bestätigt'
    redirect(action: index)
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

    // todo add wohnung to reservation
    reservation = new Reservation(guest: getGuest(), startDate: startDate, endDate: endDate)

    // todo check success status

    reservation.reserveTentatively()
    if (!reservation.success){
      String occupiedMessage = "Reservation nicht moeglich, folgende Reservationen liegen bereits vor:  </br> \n"
      reservation.existingEvents.each {entry -> occupiedMessage = occupiedMessage + entry.getTitle().getPlainText() + " -- " + new Date(entry.times[0].startTime.value) + " bis " + new Date(entry.times[0].endTime.value) + "</br>\n"}
      flash['message'] = occupiedMessage
      redirect(action: index)
    }
    reservation.sendEmailToWohnungAdministrator()

    // return success message
    flash['message'] = 'Reservation wurde eingetragen. Ein Bestätigungs-E-Mail wurde verschickt.'

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








  /**
   * After a tentative entry has been confirmed by an administrator, inform the guest that his reservation has been fixed.
   */
  void sendConfirmationEmailFor(String toAddress, java.util.Date startDate, java.util.Date endDate) {

    println toAddress
    println startDate
    println endDate

    // todo : format start and end date. use proper localisation.
    // todo add link for changing or canceling reservation
    def subject = "Wohnung reserviert: ${startDate} bis ${endDate}"
    def text = "Deine Reservation vom ${startDate} bis zum ${endDate} wurde bestätigt und definitiv reserviert"
    sendEmail(toAddress, subject, text)
  }

  /**
   * Generic email helper for erlenrain7
   */
  private void sendEmail(String emailToAddress, java.lang.String subject, java.lang.String text) {

    Properties props = new Properties();
    props.put("mail.host", "localhost");
    Session session = Session.getDefaultInstance(props, null);
    Message message = new MimeMessage(session)
    String messageType = "UTF-8"
    message.setFrom(new InternetAddress("erlenrain7@gmail.com"))
    message.setSubject(subject, messageType)
    message.setText(text, messageType)
    InternetAddress[] addressTo = new InternetAddress[1];
    addressTo[0] = new InternetAddress(emailToAddress)
    message.setRecipients(Message.RecipientType.TO, addressTo)
    message.setHeader("Content-Type", "text/plain; charset=UTF-8");
    javax.mail.Transport.send(message)

  }
}
