<%@ page contentType="text/html;charset=UTF-8" %>
<html>
  <head><title>Reservation</title></head>
  <body>
  Reservation gemacht:<br/>
  %{-- todo datum formatieren--}%
  Start Date: ${reservation.startDate} <br/>
  End Date : ${reservation.endDate} <br/>

  Du kriegst eine Bestaetigung via E-Mail an xxxxx, sobald deine Daten definitiv im Kalendar eingetragen sind.
  </body>
</html>