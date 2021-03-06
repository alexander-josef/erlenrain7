package ch.unartig.erlenrain7.domain

import com.google.gdata.client.calendar.CalendarService
import com.google.gdata.data.extensions.EventEntry
import com.google.gdata.data.extensions.When
import com.google.gdata.data.DateTime
import com.google.gdata.data.PlainTextConstruct
import ch.unartig.erlenrain7.grails.domain.User
import ch.unartig.erlenrain7.util.GDataHelper
import com.google.gdata.data.calendar.CalendarEventEntry
import com.google.gdata.data.extensions.BaseEventEntry.EventStatus
import ch.unartig.erlenrain7.grails.domain.Wohnung
import javax.servlet.http.HttpServletRequest
import ch.unartig.erlenrain7.util.Erlenrain7Util
import sample.util.CommonUtils

/**
 * Created by IntelliJ IDEA.
 * User: alexanderjosef
 * Date: Jan 21, 2009
 * Time: 3:44:40 PM
 * To change this template use File | Settings | File Templates.
 */

public class Reservation {

  // google id
  String id
  Date startDate
  Date endDate
  String title
  User guest
  Wohnung wohnung
  // todo make status enumeration
  boolean statusConfirmed
  // todo difference to id?
  String entryUrl
  // reservation successful? Set to true after successful reservation with calendar
  // todo maybe use status informatin like 0 = sucess, 1 = occupied, 2 = calendar not available etcetc.
  boolean success = false

  // todo this doesn't belong here:
  List<CalendarEventEntry> existingEvents


  /**
   *
   * Call the google calendar API and add the reservation as an event using the extra properties for the status information
   * @return success
   */
  public void reserveTentatively() {

    // todo check date constraints:
    // - start date >= today
    // - end date > start date
    // todo forward to exception view if constraints are violated

    // check availability (free-busy)
    existingEvents = checkAvailability(startDate,endDate)
    // forward to 'occupied' page in case wohnung not available
    if(existingEvents.size()>0) {
      success = false
      return
    }

    println "writing entry ...2"
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"
    def myCalendarId = "a2kndad9nrv4ceume9timpka6s@group.calendar.google.com" // id for this calendar (not the default one for the account)
    URL feedUrl = new URL("http://www.google.com/calendar/feeds/$myCalendarId/private/full")

    def myService = new CalendarService("unartig-erlenrain7-1")

    myService.setUserCredentials(myId, myPassword)

    //
    // Add an entry
    //

    // Use standard groovy magic to set the properties after construction
//    def me = new Person(name: "John Wilson", email: "tugwilson@gmail.com", uri: "http://eek.ook.org")

    EventEntry newEventEntry = new EventEntry()

    When eventTimes = new When()

    // todo check timezones !
    // todo check tzshift, see api doc
    Calendar start = Calendar.getInstance()
    start.setTime(startDate)
    start.add(Calendar.HOUR,12)
    DateTime startWhen = new DateTime(start.getTime())

    Calendar end = Calendar.getInstance()
    end.setTime(endDate)
    end.add(Calendar.HOUR,12)
    // Google treats the endDate as exclusive!
    end.add(Calendar.DAY_OF_MONTH,1)
    DateTime endWhen = new DateTime(end.getTime())

    // only the dates, no time, for all-day-event
    startWhen.setDateOnly(true)
    endWhen.setDateOnly(true)

    // no end time if one day event. End date optional
    eventTimes.setStartTime(startWhen)
    if (!startDate.equals(endDate)) {
      eventTimes.setEndTime(endWhen)
    }

    println("start of event : " + eventTimes.getStartTime())
    println("end of event : " + eventTimes.getEndTime())


    newEventEntry.setTitle(new PlainTextConstruct(this.guest.party))
    newEventEntry.setContent(new PlainTextConstruct("Reservation \"${this.wohnung}\" von ${this.guest}"))
    newEventEntry.setStatus(EventStatus.TENTATIVE)
    newEventEntry.addTime(eventTimes)

    // todo catch exception:
    // java.lang.IllegalStateException: g:when/@startTime must be less than or equal to g:when/@endTime.
	// at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:92)
    def result = myService.insert(feedUrl, newEventEntry)

    success = true;
    // todo check if logger available
    // log.info("wrote calendar entry that starts one hour from now")
//    CommonUtils.dump(newEventEntry,System.out)
//    CommonUtils.dump(result,System.out)
  }

  /**
   * When an entry is tentativly entered into the calender, the administrator gets a notification.
   */
  void sendEmailToWohnungAdministrator(HttpServletRequest request) {
    User administrator = this.wohnung.administrator
    def confirmationUrl = "http://${request.serverName}:${request.serverPort}${request.contextPath}?userId=${administrator.userId}"
    println confirmationUrl
    println("sending mail")
    String subject = "Neue Reservation für Erlenrain7"
    String messageContent = "${this.guest} hat eine neue Reservation gemacht. Bitte auf folgenden Link klicken, um die Reservation anzusehen und zu bestätigen: \n\n ${confirmationUrl}"
    Erlenrain7Util.sendEmail(administrator.email, subject, messageContent.toString())
    println("sent email to ${administrator.email}")
  }

  /**
   * check for existing events during the given reservation period. Return them so they can be shown to the requestor.
   * Todo: move out the google dependancy
   */
  List<CalendarEventEntry> checkAvailability(Date startTime, Date endTime) {
    GDataHelper.readReservationEntriesForPeriod(startTime,endTime)
  }
}