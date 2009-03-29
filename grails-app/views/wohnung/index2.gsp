<html>
<head>
  <title>Welcome to erlenrain7</title>
  <meta name="layout" content="main_topnav_fluid960"/>
</head>
<body>

<div class="grid_12">

  <h2 id="page-heading">Erlenrain7, Wohnungsreservation</h2>
</div>
<div class="clear"></div>

<div class="grid_12">

  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
</div>
<div class="clear"></div>


<div class="grid_3">
  <g:if test="${guest.userIsAdmin()}">
    <div class="box">
      <h2>Offene Reservationswuensche</h2>
    </div>
    <g:if test="${listOfUnconfirmedReservations.empty}">
      <p>(Momentan keine offene Reservationswünsche)</p>
    </g:if>

    <ul>
      <g:each var="reservation" in="${listOfUnconfirmedReservations}">
        <li>Datum : <g:formatDate date="${reservation.startDate}" format="dd.MM.yyyy"/>  - <g:formatDate date="${reservation.endDate}" format="dd.MM.yyyy"/></li>
        Gast: ${reservation.guest}<br/>
        <g:link params="[googleHref:reservation.id,guestEmail:reservation.guest.email]" action="update">Bestätigen</g:link>
        <g:link params="[googleHref:reservation.id,guestEmail:reservation.guest.email]" action="deleteReservation">Löschen</g:link>
      </g:each>
    </ul>
  </g:if>
  <div class="box">
    <h2>
      <a href="#" id="toggle-paragraphs" class="hidden">Meine Reservationen</a>
    </h2>
    <div class="block" id="paragraphs">
      <p>Diese Funktion ist noch nicht vorhanden.</p>
    </div>
  </div>



  <div class="box">

    <h2>
      Neue Reservationsanfrage
    </h2>

    <div class="block" id="reservation">

      <g:form controller="wohnung" action="reserve" method="post">
        <fieldset class="login">

          <legend>Reservation</legend>
            <!--TODO replace the datepicker with a calendar dhtml picker, like the one from yahoo ui (http://developer.yahoo.com/yui/calendar/) -->
          <p>Vom <g:datePicker precision="day" name="reservationStartDate"/></p>
           <p> Bis  <g:datePicker precision="day" name="reservationEndDate"/> </p>
            %{-- todo also option fuer end-Datum Anzahl Tage anbieten --}%
            <input class="button" type="submit" value="abschicken"/>
        </fieldset>
      </g:form>

    </div>
  </div>
</div>

%{-- Todo Meine Reservationen anzeigen--}%
%{--<g:if test="${listOfUnconfirmedReservations.empty}">--}%
%{--<p style="margin-left:20px;padding-bottom:10px">(Momentan keine offene Reservationswünsche)</p>--}%
%{--</g:if>--}%

<div class="grid_9">
  <div class="box">
    <h2>Aktuelle Belegung</h2>
  </div>
  <!--suppress HtmlDeprecatedTag -->
  <iframe style="border:0" src="http://www.google.com/calendar/embed?src=a2kndad9nrv4ceume9timpka6s%40group.calendar.google.com&ctz=Europe/Zurich&pvttk=b57ffcc944be7a4db20ef2405d16dd1d" width="700" height="600" frameborder="0" scrolling="no"/>

</div>


<div class="clear"></div>

<div class="grid_12" id="site_info">
  <div class="box">
    Erlenrain7, (c) Alexander Josef, unartig.ch
  </div>
</div>
<div class="clear"></div>
</body>
</html>