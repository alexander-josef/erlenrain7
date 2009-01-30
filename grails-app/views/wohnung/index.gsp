<html>
<head>
    <title>Welcome to Grails</title>
    <meta name="layout" content="main"/>
</head>
<body>

<g:if test="${params.user =='admin'}">


    <p style="margin-left:20px;padding-bottom:10px;color:orangered">Administrations-Modus</p>
    <p style="margin-left:20px;padding-bottom:10px">
        <h2 style="margin-left:20px;padding-bottom:10px">Neue Reservationswünsche</h2>

    <ul style="margin-left:20px;padding-bottom:10px">
        <g:each var="reservation" in="${listOfUnconfirmedReservations}">
            <li>ID : ${reservation.id}
            (<g:link params="[googleHref:reservation.id]" action="update">update</g:link>)<br/>
                todo insert loeschen button
                URL : ${reservation.entryUrl}<br>
                GUEST : ${reservation.guest}<br>
                TITLE : ${reservation.title}<br>
                CONFIRMED: ${reservation.statusConfirmed}<br>
            </li>
        </g:each>
    </ul>

</g:if>

<h1 style="margin-left:20px;">Erlenrain7, Wohnungsreservation</h1>
<g:form controller="wohnung" action="reserve" method="post">
    <p style="margin-left:20px;width:80%">
        <!--TODO replace the datepicker with a calendar dhtml picker, like the one from yahoo ui (http://developer.yahoo.com/yui/calendar/) -->
        Neue Reservationsanfrage<br/>
        Von <g:datePicker precision="day" name="reservationStartDate"/><br/>
        Bis  <g:datePicker precision="day" name="reservationEndDate"/> <br/>
        %{-- todo also option fuer end-Datum Anzahl Tage anbieten --}%
        <input type="submit" value="abschicken"/>
    </p>
</g:form>
%{--<g:link controller="wohnung" action="reserve">link to reserve</g:link>--}%

%{--<div class="dialog" style="margin-left:20px;width:60%;">--}%
%{--<ul>--}%
%{--<g:each var="c" in="${grailsApplication.controllerClasses}">--}%
%{--<li class="controller"><g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link></li>--}%
%{--</g:each>--}%
%{--</ul>--}%
%{--</div>--}%



<p style="margin-left:20px;padding-bottom:10px">Aktuelle Belegung:

</p>

... later
%{-- AJ's account: --}%
%{--<iframe style="margin-left:20px;width:60%;border:0" src="http://www.google.com/calendar/embed?src=cpjjds3o8nfjeafft2ole89ip4%40group.calendar.google.com&ctz=Europe/Rome&pvttk=2817c248a6d1d47e8b516d6ca32dad0b" width="800" height="600" frameborder="0" scrolling="no"/>--}%

%{-- erlenrain7 account : --}%
%{--<iframe style="margin-left:20px;width:60%;border:0" src="http://www.google.com/calendar/embed?src=a2kndad9nrv4ceume9timpka6s%40group.calendar.google.com&ctz=Europe/Zurich&pvttk=b57ffcc944be7a4db20ef2405d16dd1d" width="800" height="600" frameborder="0" scrolling="no"/>--}%
</body>
</html>