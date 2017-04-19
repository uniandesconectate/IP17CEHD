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
	<header>
		<div class="container">
			<div class="row vertical-align">
				<div class="col-xs-12 col-md-11">
					<h1><g:link uri="/">Centro de Español</g:link></h1>
				</div>
				<sec:ifLoggedIn>
					<div class="col-xs-12 col-md-1">
						<h4><g:link controller="logout">Salir</g:link></h4>
					</div>
				</sec:ifLoggedIn>
			</div>
		</div>
	</header>
	<div class="container">
		<g:if test="${flash.error}">
			<div class="alert alert-danger" role="alert">
				${raw(flash.error)}
			</div>
		</g:if>
		<g:if test="${flash.message}">
			<div class="alert alert-info" role="alert">
				${raw(flash.message)}
			</div>
		</g:if>
		<g:layoutBody/>
	</div>
	<footer>
		<div class="container">
			<div class="col-xs-12 col-sm-3">
				<a href="http://uniandes.edu.co/" target="_blank">
					<asset:image src="uniandes.png" alt="Universidad de Los Andes"/>
				</a>
			</div>
			<div class="col-xs-12 col-sm-6">
				Universidad de los Andes | Vigilada Mineducación<br />
				Reconocimiento como Universidad: Decreto 1297 del 30 de mayo de 1964.<br />
				Reconocimiento personería jurídica: Resolución 28 del 23 de febrero de 1949 Minjusticia.<br />
				Cra 1 Nº 18A- 12 Bogotá, (Colombia) | Código postal: 111711 | Tels: +571 3394949 - +571 3394999
			</div>
			<div class="col-xs-12 col-sm-3">
				<a href="http://conectate.uniandes.edu.co/" target="_blank">
				<asset:image src="conectate.png" alt="Conecta-TE" />
				</a>
			</div>
		</div>
	</footer>
	</body>
</html>
