<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${createLinkTo(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${createLinkTo(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />
    </head>
<body>
<div id="spinner" class="spinner" style="display:none;">
    <img src="${createLinkTo(dir:'images',file:'spinner.gif')}" alt="Spinner" />
        </div>
        <div class="logo"><img src="${createLinkTo(dir:'images',file:'grails_logo.jpg')}" alt="Grails" /></div>
    <div class="nav">
        <span class="menuButton"><a class="home" href="${createLinkTo(dir:'')}">Home</a></span>
        <span class="menuButton"><g:link controller="user" action="logout">Logout</g:link></span>
        <g:if test="${guest.userIsAdmin()}">
            <span class="menuButton" style="color:orangered">Administrations-Modus</span>
        </g:if>
    </div>

        <g:layoutBody />
    </body>
</html>