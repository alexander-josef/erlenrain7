package ch.unartig.erlenrain7.util

import com.google.gdata.data.extensions.EventEntry
import com.google.gdata.client.calendar.CalendarService
import com.google.gdata.data.TextConstruct
import com.google.gdata.data.PlainTextConstruct
import com.google.gdata.client.calendar.CalendarQuery
import com.google.gdata.data.DateTime
import ch.unartig.erlenrain7.domain.Reservation
import com.google.gdata.data.calendar.CalendarEventFeed
import com.google.gdata.data.extensions.BaseEventEntry.EventStatus
import com.google.gdata.data.calendar.CalendarEventEntry

/**
 * Created by IntelliJ IDEA.
 * User: alexanderjosef
 * Date: Feb 19, 2009
 * Time: 7:15:14 PM
 * To change this template use File | Settings | File Templates.
 */

public class GDataHelper {

  static ArrayList readUnconfirmedEntries(logggedInGuest) {
    def listOfUnconfirmedReservations = new ArrayList()
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"
    def myCalendarId = "a2kndad9nrv4ceume9timpka6s@group.calendar.google.com" // id for this calendar (not the default one for the account)
    URL feedUrl = new URL("http://www.google.com/calendar/feeds/$myCalendarId/private/full")

    def myService = new CalendarService("unartig-erlenrain7-1")

    myService.setUserCredentials(myId, myPassword)

    //
    // List existing entries
    //

    println("Reading Calendar entries from Google Calendar ....")

    // query event entries from last week on that are tentative

    CalendarQuery myQuery = new CalendarQuery(feedUrl)
    def today = new Date()
    // show all entries that have a start date not more than one week ago
    myQuery.setMinimumStartTime(new DateTime(today.minus(7)))
    myService.query(myQuery, CalendarEventFeed.class)

    myService.query(myQuery, CalendarEventFeed.class).entries.each {entry ->
      entry.times.each {time ->

        println(new Date(time.startTime.value))
//        if (entry.status.equals(EventStatus.TENTATIVE)) {
        if (true) { // todo replace with real if
          listOfUnconfirmedReservations.add(new Reservation(
                  guest: logggedInGuest,
                  startDate: new Date(time.startTime.value),
                  title: entry.getTitle().getPlainText(),
                  endDate: new Date(time.endTime.value),
                  id: entry.id,
                  statusConfirmed: !entry.status.equals(EventStatus.TENTATIVE),
                  entryUrl: entry.selfLink.href
          )

          )
        }
        println "${entry.title.text} From: ${time.startTime.toUiString()} To: ${(time.endTime.toUiString())}"
        println "${entry.title.text}"
      }
    }

    return listOfUnconfirmedReservations
  }

  /**
   * Delete a reservation given its ID (event entry URL)
   */
  static deleteEventEntry(String reservationId) {
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"
    def myService = new CalendarService("unartig-erlenrain7-1")
    myService.setUserCredentials(myId, myPassword)


    URL entryUrl = new URL(reservationId)

    EventEntry entry = myService.getEntry(entryUrl, EventEntry.class)

    URL deleteUrl = new URL(entry.getEditLink().getHref());
    myService.delete(deleteUrl)
  }



  static EventEntry updateReservationEventEntry(String reservationId) {
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"
    URL entryUrl = new URL(reservationId)

    def myService = new CalendarService("unartig-erlenrain7-1")

    myService.setUserCredentials(myId, myPassword)

    EventEntry entry = myService.getEntry(entryUrl, EventEntry.class)

    println("Resource ID : " + entry.getId());

    String titleText = entry.getTitle().getPlainText()
    TextConstruct summary = entry.getSummary()
    println(titleText)
    println(entry.getContent()?.toString())
    println(summary?.getPlainText())

    entry.setTitle(new PlainTextConstruct(titleText + "(bestätigt)"))
    // update status to confirmed
    entry.setStatus(EventStatus.CONFIRMED)

    // todo later carry processing info in content field, like "Reservation bestaetigt von Doelf, 21.10.2009"
    // save

    // there needs to be a new version, therefore we need to get a new href
    URL editUrl = new URL(entry.getEditLink().getHref());
    myService.update(editUrl, entry)
    return entry
  }


  /**
   *
   */
  static public List<CalendarEventEntry> readReservationEntriesForPeriod(Date startTime, Date endTime) {
    def listOfExistingReservations = new ArrayList()
    def myId = "erlenrain7@unartig.ch"
    def myPassword = "noedsichaer"
    def myCalendarId = "a2kndad9nrv4ceume9timpka6s@group.calendar.google.com" // id for this calendar (not the default one for the account)
    URL feedUrl = new URL("http://www.google.com/calendar/feeds/$myCalendarId/private/full")

    def myService = new CalendarService("unartig-erlenrain7-1")

    myService.setUserCredentials(myId, myPassword)

    //
    // List existing entries
    //

    println("Reading Calendar entries from Google Calendar ....")

    // query event entries from last week on that are tentative

    CalendarQuery myQuery = new CalendarQuery(feedUrl)
    // query for start/end times
    myQuery.setMinimumStartTime(new DateTime(startTime))
    myQuery.setMaximumStartTime(new DateTime(endTime))
    myService.query(myQuery, CalendarEventFeed.class)

    List entries = myService.query(myQuery, CalendarEventFeed.class).entries
    entries.each {entry ->
      entry.times.each {time ->


        println(new Date(time.startTime.value))
//        listOfExistingReservations.add(new Reservation(
//                guest: logggedInGuest,
//                startDate: new Date(time.startTime.value),
//                title: entry.getTitle().getPlainText(),
//                endDate: new Date(time.endTime.value),
//                id: entry.id,
//                statusConfirmed: !entry.status.equals(EventStatus.TENTATIVE),
//                entryUrl: entry.selfLink.href
//        )

//        )
        println "${entry.title.text} From: ${time.startTime.toUiString()} To: ${(time.endTime.toUiString())}"
        println "${entry.title.text}"
      }
    }

    return entries
  }


}