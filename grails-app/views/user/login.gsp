<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <meta name="layout" content="main_home">
  <title>Log in to erlenrain7</title></head>
<body>

<div class="body">
  <h1>Bitte anmelden</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
    %{-- todo check this (race bean?) : --}%
  <g:hasErrors bean="${race}">
    <div class="errors">
      <g:renderErrors bean="${user}" as="list"/>
    </div>
  </g:hasErrors>
  <g:form controller="user" method="post">
    <div class="dialog">
      <table>
        <tr class='prop'>
          <td valign='top' class='name'>
            <label for='email'>E-Mail:</label>
          </td>
          <td valign='top' class='value '>
            <input id="email" type="text" maxlength='40'
                    name='email'
                    value='${user?.email}' />
          </td>
        </tr>
        <tr class='prop'>
          <td valign='top' class='name'>
            <label for='password'>Password:</label>
          </td>
          <td valign='top' class='value '>
            <input id="password" type="password" maxlength='15'
                    name='password'
                    value='${user?.password}'/>
          </td>
        </tr>
      </table>
    </div>

    <div class="buttons">
      <span class="button">
        <g:actionSubmit value="Log in"/>
      </span>
    </div>
  </g:form>
</div>

</body>
</html>