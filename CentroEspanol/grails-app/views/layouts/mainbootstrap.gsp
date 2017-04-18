<!DOCTYPE html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="Grails"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
  		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<g:layoutHead/>
	</head>
	<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
	    	<div class="navbar-header">
	      		<a class="navbar-brand" href="/CentroEspanol">Centro de Español</a>
	    	</div>
	    	<sec:ifLoggedIn>
      			<ul class="nav navbar-nav navbar-right">
       				<li><g:link controller="logout" class="register">Logout</g:link></li>
   				</ul>
			</sec:ifLoggedIn>
	  	</div>
	</nav>
		<div class="container">
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="alert">
					${flash.error}
				</div>
			</g:if>
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="alert">
					${flash.message}
				</div>
			</g:if>
			<g:layoutBody/>
		</div>
	</body>
</html>
