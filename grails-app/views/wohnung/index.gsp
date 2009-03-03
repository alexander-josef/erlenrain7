<html>
<head>
  <title>Welcome to erlenrain7</title>
  <meta name="layout" content="main_topnav"/>
</head>
<body>
<p style="margin-left:20px">

  <!-- todo table (or div?) layout -->
  Hallo ${guest}
</p>
<h1 style="margin-left:20px;">Erlenrain7, Wohnungsreservation</h1>

<p style="margin-left:20px;padding-bottom:10px">

<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
</g:if>


<g:if test="${guest.userIsAdmin()}">

%{--<p style="margin-left:20px;padding-bottom:10px;color:orangered">Administrations-Modus</p>--}%

  <h3 style="margin-left:20px;padding-bottom:10px">Offene Reservationswünsche</h3>
  <g:if test="${listOfUnconfirmedReservations.empty}">
    <p style="margin-left:20px;padding-bottom:10px">(Momentan keine offene Reservationswünsche)</p>
  </g:if>

  <ul style="margin-left:20px;padding-bottom:10px">
    <g:each var="reservation" in="${listOfUnconfirmedReservations}">
      <li>Datum : <g:formatDate date="${reservation.startDate}" format="dd.MM.yyyy"/>  - <g:formatDate date="${reservation.endDate}" format="dd.MM.yyyy"/></li>
      Gast: ${reservation.guest}<br/>
      <g:link params="[googleHref:reservation.id,guestEmail:reservation.guest.email]" action="update">Bestätigen</g:link>
      <g:link params="[googleHref:reservation.id,guestEmail:reservation.guest.email]" action="deleteReservation">Löschen</g:link>
    </g:each>
  </ul>

</g:if>

<h3 style="margin-left:20px;padding-bottom:10px">Meine Reservationen</h3>
(Diese Funktion ist noch nicht vorhanden)
%{-- Todo Meine Reservationen anzeigen--}%
%{--<g:if test="${listOfUnconfirmedReservations.empty}">--}%
%{--<p style="margin-left:20px;padding-bottom:10px">(Momentan keine offene Reservationswünsche)</p>--}%
%{--</g:if>--}%


<g:form controller="wohnung" action="reserve" method="post">

  <h3 style="margin-left:20px;padding-bottom:10px">Neue Reservationsanfrage</h3>

  <p style="margin-left:20px;width:80%">
    <!--TODO replace the datepicker with a calendar dhtml picker, like the one from yahoo ui (http://developer.yahoo.com/yui/calendar/) -->
    Vom <g:datePicker precision="day" name="reservationStartDate"/><br/>
    Bis  <g:datePicker precision="day" name="reservationEndDate"/> <br/>
    %{-- todo also option fuer end-Datum Anzahl Tage anbieten --}%
    <input type="submit" value="abschicken"/>
  </p>
</g:form>

<p style="margin-left:20px;padding-bottom:10px">Aktuelle Belegung:

</p>

... later

%{-- erlenrain7 account : --}%
%{--<iframe style="margin-left:20px;width:60%;border:0" src="http://www.google.com/calendar/embed?src=a2kndad9nrv4ceume9timpka6s%40group.calendar.google.com&ctz=Europe/Zurich&pvttk=b57ffcc944be7a4db20ef2405d16dd1d" width="800" height="600" frameborder="0" scrolling="no"/>--}%
</body>
</html>