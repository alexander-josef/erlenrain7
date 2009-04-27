<html>
<head>
  <title>Welcome to erlenrain7</title>
  <meta name="layout" content="main_topnav_fluid960"/>


  <gui:resources components="['datePicker']"/>

  <style type="text/css">

  .datefield {
    width: 7em;
  }

  .datefield input {
    width: 6em;
  }

  %{-- the datepicker inside the fieldset is in the background ... how can we solve that? --}%

  fieldset {
    position: static;
  }
  </style>

</head>
<body class=" yui-skin-sam">

<div class="grid_12">
  <h2 id="page-heading">Erlenrain7, Wohnungsreservation</h2>
</div>

<div class="clear"></div>


<g:if test="${flash.message}">
  <div class="grid_12">
    <div class="message">${flash.message}</div>
  </div>
  <div class="clear"></div>
</g:if>


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
      <a href="#" id="toggle-myReservations">Meine Reservationen</a>
    </h2>
    <div class="block" id="myReservations">
      <p>Diese Funktion ist noch nicht vorhanden. Todo.</p>
    </div>
  </div>



  <div class="box">

    <h2>
      Neue Reservationsanfrage
    </h2>

    <div class="block" id="reservation">

      <g:form controller="wohnung" action="reserve" method="post">

        <fieldset>

          <legend>Reservation</legend>

          <label for="startReservationDatePicker" style="margin-top: 18px; float:left; width: 40px;">Von:</label>
          <gui:datePicker id='startReservationDatePicker'></gui:datePicker>
          <label for="endReservationDatePicker" style="margin-top: 18px; float:left; width: 40px;">
          Bis:</label><gui:datePicker id='endReservationDatePicker'></gui:datePicker>
          %{-- todo also option fuer end-Datum Anzahl Tage anbieten --}%
          <br/>
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
  <div>
    <!--suppress HtmlDeprecatedTag -->
    <iframe
            src="http://www.google.com/calendar/embed?src=a2kndad9nrv4ceume9timpka6s%40group.calendar.google.com&ctz=Europe/Zurich&pvttk=b57ffcc944be7a4db20ef2405d16dd1d"
            width="700"
            height="500"
            frameborder="0"
            scrolling="no">
    </iframe>
    %{-- empty closing tag needed !--}%

  </div>

</div>
<div class="clear"></div>

<div class="grid_12" id="site_info">
  <div class="box">
    Erlenrain7, (c) 2009 Alexander Josef, <a href="http://unartig.ch">unartig.ch</a>
  </div>
</div>
<div class="clear"></div>
</body>
</html>