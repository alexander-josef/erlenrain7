<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>

  <title><g:layoutTitle default="Grails"/></title>

  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir: '960/css', file: 'reset.css')}" media="screen"/>
  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir: '960/css', file: 'text.css')}" media="screen"/>
  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir: '960/css', file: '960.css')}" media="screen"/>
  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir: '960/css', file: 'layout.css')}" media="screen"/>
  <link rel="stylesheet" type="text/css" href="${createLinkTo(dir: '960/css', file: 'nav.css')}" media="screen"/>
  <script type="text/javascript" src="${createLinkTo(dir: '960/js', file: 'mootools.js')}"></script>
  <script type="text/javascript" src="${createLinkTo(dir: '960/js', file: '960.js')}"></script>
  <script type="text/javascript" src="${createLinkTo(dir: '960/js', file: 'kwick.js')}"></script>
  <script type="text/javascript" src="${createLinkTo(dir: '960/js', file: 'accordion.js')}"></script>
  <script type="text/javascript" src="${createLinkTo(dir: '960/js', file: 'sectionmenu.js')}"></script>

  %{--<link rel="stylesheet" href="${createLinkTo(dir: 'css', file: 'main.css')}"/>--}%
  <link rel="shortcut icon" href="${createLinkTo(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
  <g:layoutHead/>
  %{--<g:javascript library="application"/>--}%
</head>
<body class="yui-skin-sam">
%{--<div id="spinner" class="spinner" style="display:none;">--}%
  %{--<img src="${createLinkTo(dir: 'images', file: 'spinner.gif')}" alt="Spinner"/>--}%
%{--</div>--}%
<div class="container_12">

  <div class="grid_12">
    <img src="${createLinkTo(dir: 'images', file: 'hahnen_panorama.jpg')}" alt="Erlenrain7-Wohnungsreservation"/>
  </div>
  <div class="clear"></div>

  <div class="grid_12">
    <ul class="nav main">
      <li>
        <a href="${createLinkTo(dir: '')}">Home</a>
      </li>
      <li class="secondary">
        <g:link controller="user" action="logout">Logout</g:link>
      </li>
      <li class="secondary">
        Hallo ${guest}
      </li>

      <g:if test="${guest.userIsAdmin()}">
        <li class="secondary">
          <p>Administrations-Modus</p>
        </li>

      </g:if>

    </ul>

  </div>
  <div class="clear"></div>

  <g:layoutBody/>
</div>

</body>
</html>