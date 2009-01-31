//import groovy.google.gdata.GDataCategory

import com.google.gdata.client.calendar.CalendarService
import com.google.gdata.data.Person
import com.google.gdata.data.extensions.EventEntry
import com.google.gdata.data.extensions.When
import com.google.gdata.data.calendar.CalendarFeed
import com.google.gdata.data.calendar.CalendarEntry
import com.google.gdata.data.calendar.CalendarEventEntry
import com.google.gdata.data.calendar.CalendarEventFeed
import java.text.SimpleDateFormat
import com.google.gdata.data.extensions.BaseEventEntry.EventStatus
import com.google.gdata.data.TextConstruct
import com.google.gdata.data.PlainTextConstruct
import com.google.gdata.data.DateTime
import com.google.gdata.client.Query
import com.google.gdata.client.calendar.CalendarQuery
import javax.mail.Session
import javax.mail.Message
import javax.mail.internet.MimeMessage
import javax.mail.Address
import javax.mail.internet.InternetAddress


class WohnungController {

    // todo replace with method to retrieve persisted guest object
    def guest = new Guest(firstName: "Hans", lastName: "Test", email: "aj@unartig.ch", phone: "0719200745")
    def administrator = new Guest(firstName: "Hans", lastName: "Test", email: "aj@unartig.ch", phone: "0719200745")

    def reservation
    def listOfUnconfirmedReservations
    def index = {
        readCalendarEntries()
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

        println("8updating event with id : " + params.googleHref + "...")

        // id is urlencoded, translate back

//        def reservationId = URLDecoder.decode(params.id)
        String reservationId = params.googleHref
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

        sendConfirmationEmailFor(params.guestEmail,new Date(entry.times[0].startTime.value),new Date(entry.times[0].endTime.value))


    }

    /**
     * Process the input (start and end date) and create a reservation object that is used to make a google calendar entry.
     */
    def reserve = {
        println "... making reservation"

        String startDateInput = "${params.reservationStartDate_year}/${params.reservationStartDate_month}/${params.reservationStartDate_day}"
        String endDateInput = "${params.reservationEndDate_year}/${params.reservationEndDate_month}/${params.reservationEndDate_day}"
        Date startDate = new SimpleDateFormat("yyyy/MM/dd").parse(startDateInput)
        Date endDate = new SimpleDateFormat("yyyy/MM/dd").parse(endDateInput)
//    render(startDate)
//    render(endDate)

        // make entry to calendar with status tentativly

        reservation = new Reservation(guest:guest, startDate: startDate, endDate: endDate)

        // todo check date constraints:
        // - start date >= today
        // - end date > start date
        // todo forward to exception view if constraints are violated

        // todo check availability (free-busy)
        // todo forward to 'occupied' page in case wohnung not available

        reserveTentatively(reservation)

        sendEmailToWohnungAdministrator(reservation)

        // todo return success message
        // or use redirect?
//        render(view:'reservationSuccess',model:[reservation:reservation])

        println(reservation.guest)

//    render(reservation)

    }

    /**
     * My first groovy and GDATA code
     * What does it do ?
     * It currently reads the entries from the separate google account "erlenrain7@unartig.ch"
     *
     */
    void readCalendarEntries() {




        listOfUnconfirmedReservations = new ArrayList()
        def myId = "erlenrain7@unartig.ch"
        def myPassword = "noedsichaer"
        def myCalendarId = "a2kndad9nrv4ceume9timpka6s@group.calendar.google.com" // id for this calendar (not the default one for the account)
        URL feedUrl = new URL("http://www.google.com/calendar/feeds/$myCalendarId/private/full")

        def myService = new CalendarService("unartig-erlenrain7-1")

        myService.setUserCredentials(myId, myPassword)

        //
        // List existing entries
        //

        //
        //  Get at most 20 events in the period starting 1 week ago and ending 4 weeks in the future
        //
        println("Reading Calendar entries from Google Calendar ....")

        // todo query event entries from today that are tentative

        CalendarQuery myQuery = new CalendarQuery(feedUrl)
        def today = new Date()
        // show all entries that have a start date not more than one week ago
        myQuery.setMinimumStartTime(new DateTime(today.minus(7)))
        myService.query(myQuery, CalendarEventFeed.class)

//    myService.getFeed(feedUrl, CalendarEventFeed.class).entries.each {entry ->
        myService.query(myQuery, CalendarEventFeed.class).entries.each {entry ->
            entry.times.each {time ->

                println(new Date(time.startTime.value))
                if (entry.status.equals(EventStatus.TENTATIVE)) {
                    listOfUnconfirmedReservations.add(new Reservation(
                            guest: guest,
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

//        CalendarEventFeed myFeed = myService.getFeed(feedUrl,CalendarEventFeed.class)
//
//        for(obj in myFeed.getEntries()) {
//            CalendarEventEntry calEntry = (CalendarEventEntry)obj
//            println calEntry.getClass().getName()
//            println "and now the times ..."
//            println calEntry.getTimes().get(0).startTime.toUiString()
//            println calEntry.getTimes().get(0).endTime.toUiString()
//
//        }

        //
        //  Get at most 20 events in the period starting 1 year ago lasting 2 years
        //
//        myService.getFeed(feedUrl, 1.year.ago, 2.years, 20).entries.each {entry ->
//            entry.times.each {time ->
//                println "${entry.title.text} From: ${time.startTime.toUiString()} To: ${(time.endTime.toUiString())}"
//            }
//        }

    }





    /**
     * Call the google calendar API and add the reservation as an event using the extra properties for the status information
     */
    private void reserveTentatively(Reservation p) {
        // todo can reservation be hidden, i.e. only visible by administration and requestor until confirmed?

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
        def me = new Person(name: "John Wilson", email: "tugwilson@gmail.com", uri: "http://eek.ook.org")

        //
        // Need special magic in the GDataCategory to do this
        //
        // title and content are treated as plain text. If you want XHTML or XML then pass a closure or a
        // Buildable object and it will run it in a builder context
        //
        // Note that we can't use title and content in the Catagory as they are already properties of the class.
        // Later I'll create a custom MetaClass for EventEntry which will let us use these names. Until then we'll mangle them
        //
        // author can be a single Person or a list of Person
        //
        // time can be a single When or a list of them
        //


        def newEventEntry = new EventEntry()

        When eventTimes = new When()




        def startDate = new DateTime(new Date())
        def endDate = new DateTime(new Date().plus(1))

        eventTimes.setStartTime(startDate)
        eventTimes.setEndTime(endDate)

        println("start of event : " + eventTimes.getStartTime())
        println("end of event : " + eventTimes.getEndTime())


        newEventEntry.setTitle(new PlainTextConstruct("Test Entry Number one"))
        newEventEntry.setContent(new PlainTextConstruct(" ... some content ...."))
        newEventEntry.setStatus(EventStatus.TENTATIVE) // todo check this !!
        newEventEntry.addTime(eventTimes)

        // todo set hidden property (not available on eventEntry? maybe only no calendar entry)
//    newEventEntry.setHidde)
//        def newEntry = new EventEntry(title1: "This is a test event", content1: "this is some content", author: me,
//                time: new When(start: 1.hour.from.now, end: 2.hours.from.now))

        myService.insert(feedUrl, newEventEntry)

        println "wrote calendar entry that starts one hour from now"

    }

    void sendEmailToWohnungAdministrator(Reservation reservation) {
        // init sesssion
        def confirmationUrl = "http://${request.serverName}:${request.serverPort}${request.contextPath}?user=admin"
        println confirmationUrl
        println("sending mail")
        String subject = "Neue Reservation für Erlenrain7"
        String messageContent = "${reservation.guest} hat eine neue Reservation gemacht. Bitte auf folgenden Link klicken, um die Reservation anzusehen und zu bestätigen: \n\n ${confirmationUrl}"

        sendEmail(administrator.email,subject,messageContent.toString())
        println("sent email to ${administrator.email}")
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
        def subject="Wohnung reserviert: ${startDate} bis ${endDate}"
        def text="Deine Reservation vom ${startDate} bis zum ${endDate} wurde bestätigt und definitiv reserviert"
        sendEmail(toAddress,subject,text)
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
        message.setSubject(subject,messageType)
        message.setText(text,messageType)
        InternetAddress[] addressTo = new InternetAddress[1];
        addressTo[0] = new InternetAddress(emailToAddress)
        message.setRecipients(Message.RecipientType.TO,addressTo)
        message.setHeader("Content-Type", "text/plain; charset=UTF-8");
        javax.mail.Transport.send(message)

    }
}
