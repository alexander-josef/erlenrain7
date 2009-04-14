<html>
<head>
  <title>Welcome to erlenrain7</title>
  <meta name="layout" content="main_topnav_fluid960"/>
  <g:javascript library="yui"/>
  <yui:javascript dir="calendar" file="calendar-min.js"/>
  %{--<yui:stylesheet dir="calendar/assets" file="calendar.css"/>--}%
  <yui:stylesheet dir="calendar/assets/skins/sam" file="calendar.css"/>

  <yui:javascript dir="button" file="button-min.js"/>
  %{--<yui:stylesheet dir="button/assets" file="button.css"/>--}%
  <yui:stylesheet dir="button/assets/skins/sam" file="button.css"/>

  <yui:javascript dir="container" file="container-min.js"/>
  %{--<yui:stylesheet dir="container/assets" file="container.css"/>--}%
  <yui:stylesheet dir="container/assets/skins/sam" file="container.css"/>

  <yui:javascript dir="event" file="event.js"/>
  <yui:javascript dir="dragdrop" file="dragdrop.js"/>
  <yui:javascript dir="element" file="element.js"/>

  <yui:stylesheet dir="fonts" file="fonts-min.css"/>

  <calendar:resources lang="en" theme="tiger"/>


  <!--begin custom header content for this example-->
  <style type="text/css">
/* Clear calendar's float, using dialog inbuilt form element */
#container .bd form {
    clear:left;
}

/* Have calendar squeeze upto bd bounding box */
#container .bd {
    padding:0;
}

#container .hd {
    text-align:left;
}

/* Center buttons in the footer */
#container .ft .button-group {
    text-align:center;
}

/* Prevent border-collapse:collapse from bleeding through in IE6, IE7 */
#container_c.yui-overlay-hidden table {
    *display:none;
}

/* Remove calendar's border and set padding in ems instead of px, so we can specify an width in ems for the container */
#cal {
    border:none;
    padding:1em;
}

/* Datefield look/feel */
.datefield {
    position:relative;
    top:10px;
    left:10px;
    white-space:nowrap;
    border:1px solid black;
    background-color:#eee;
    width:25em;
    padding:5px;
}

.datefield input,
.datefield button,
.datefield label  {
    vertical-align:middle;
}

.datefield label  {
    font-weight:bold;
}

.datefield input  {
    width:15em;
}

.datefield button  {
    padding:0 5px 0 5px;
    margin-left:2px;
}

.datefield button img {
    padding:0;
    margin:0;
    vertical-align:middle;
}

  /* Example box */
  .dfbox {
    position: relative;
    height: 30em;
  }
  </style>

</head>
<body class=" yui-skin-sam">

<!--BEGIN SOURCE CODE FOR EXAMPLE =============================== -->

<script type="text/javascript">
  YAHOO.util.Event.onDOMReady(function() {

    var Event = YAHOO.util.Event,
            Dom = YAHOO.util.Dom,
            dialog,
            calendar;

    var showBtn = Dom.get("show");

    Event.on(showBtn, "click", function() {

      // Lazy Dialog Creation - Wait to create the Dialog, and setup document click listeners, until the first time the button is clicked.
      if (!dialog) {

        // Hide Calendar if we click anywhere in the document other than the calendar
        Event.on(document, "click", function(e) {
          var el = Event.getTarget(e);
          var dialogEl = dialog.element;
          if (el != dialogEl && !Dom.isAncestor(dialogEl, el) && el != showBtn && !Dom.isAncestor(showBtn, el)) {
            dialog.hide();
          }
        });

        function resetHandler() {
          // Reset the current calendar page to the select date, or
          // to today if nothing is selected.
          var selDates = calendar.getSelectedDates();
          var resetDate;

          if (selDates.length > 0) {
            resetDate = selDates[0];
          } else {
            resetDate = calendar.today;
          }

          calendar.cfg.setProperty("pagedate", resetDate);
          calendar.render();
        }

        function closeHandler() {
          dialog.hide();
        }

        dialog = new YAHOO.widget.Dialog("container", {
          visible:false,
          context:["show", "tl", "bl"],
          buttons:[ {text:"Reset", handler: resetHandler, isDefault:true}, {text:"Close", handler: closeHandler}],
          draggable:false,
          close:true
        });
        dialog.setHeader('Datum wählen');
        dialog.setBody('<div id="cal"></div>');
        dialog.render(document.body);

        dialog.showEvent.subscribe(function() {
          if (YAHOO.env.ua.ie) {
            // Since we're hiding the table using yui-overlay-hidden, we
            // want to let the dialog know that the content size has changed, when
            // shown
            dialog.fireEvent("changeContent");
          }
        });
      }

      // Lazy Calendar Creation - Wait to create the Calendar until the first time the button is clicked.
      if (!calendar) {

        calendar = new YAHOO.widget.Calendar("cal", {
          iframe:false,          // Turn iframe off, since container has iframe support.
          hide_blank_weeks:true  // Enable, to demonstrate how we handle changing height, using changeContent
        });
        calendar.render();

        calendar.selectEvent.subscribe(function() {
          if (calendar.getSelectedDates().length > 0) {

            var selDate = calendar.getSelectedDates()[0];

            // Pretty Date Output, using Calendar's Locale values: Friday, 8 February 2008
            var wStr = calendar.cfg.getProperty("WEEKDAYS_LONG")[selDate.getDay()];
            var dStr = selDate.getDate();
            var mStr = calendar.cfg.getProperty("MONTHS_LONG")[selDate.getMonth()];
            var yStr = selDate.getFullYear();

            var day = selDate.getDate();
            var month = selDate.getMonth();
            var year = selDate.getFullYear();

            alert("day: "+day+ " month: "+month+" year: "+year);


//            Dom.get("date").value = wStr + ", " + dStr + " " + mStr + " " + yStr;
            Dom.get("date").value = wStr + ", ##" + dStr + " " + mStr + " " + yStr;
          } else {
            Dom.get("date").value = "";
          }
          dialog.hide();
        });

        calendar.renderEvent.subscribe(function() {
          // Tell Dialog it's contents have changed, which allows
          // container to redraw the underlay (for IE6/Safari2)
          dialog.fireEvent("changeContent");
        });
      }

      var seldate = calendar.getSelectedDates();

      if (seldate.length > 0) {
        // Set the pagedate to show the selected date if it exists
        calendar.cfg.setProperty("pagedate", seldate[0]);
        calendar.render();
      }

      dialog.show();
    });
  });
</script>


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
        <fieldset class="login">

          <legend>Reservation</legend>
          <!--TODO replace the datepicker with a calendar dhtml picker, like the one from yahoo ui (http://developer.yahoo.com/yui/calendar/) -->
          <p>Vom <g:datePicker precision="day" name="reservationStartDate"/></p>
          <p>Bis  <g:datePicker precision="day" name="reservationEndDate"/></p>
          %{-- todo also option fuer end-Datum Anzahl Tage anbieten --}%
          <input class="button" type="submit" value="abschicken"/>
        </fieldset>

        <div class="box">
          <calendar:datePicker name="datePick" defaultValue="${new Date()}"/>
        </div>

        <div class="dfbox">
          <div class="datefield">
            <p>
              <label for="date">Date:</label>
              <input type="text" id="date" name="date"/>
            </p>
            <button type="button" id="show" title="Show Calendar">
              <img src="assets/calbtn.gif" width="18" height="18" alt="Calendar">

            </button>
          </div>
        </div>

        <div id="cal1Container"></div>
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
  <iframe
          src="http://www.google.com/calendar/embed?src=a2kndad9nrv4ceume9timpka6s%40group.calendar.google.com&ctz=Europe/Zurich&pvttk=b57ffcc944be7a4db20ef2405d16dd1d"
          width="700"
          height="500"
          frameborder="0"
          scrolling="no">
  </iframe>
%{-- closing tag needed !--}%
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