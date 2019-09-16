<%@ page import="co.edu.uniandes.login.Role" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<title>CentroEspañol</title>
	</head>
	<body>
		<sec:ifNotLoggedIn>
			${response.sendRedirect(createLink(controller: 'login', action: 'auth'))}
		</sec:ifNotLoggedIn>
		<sec:ifLoggedIn>
			<h2>Funcionalidades</h2>
			<ul>
				<sec:ifAllGranted roles="${Role.ROLE_ADMIN}">
					<li><g:link controller="centroEspanol" action="subirInformacion">Subir información</g:link></li>
					<li><g:link controller="centroEspanol" action="reporteGeneral">Reporte general</g:link></li>
				</sec:ifAllGranted>
				<sec:ifAllGranted roles="${Role.ROLE_EVALUADOR}">
					<li><g:link controller="evaluacion" action="verEvaluacionesPendientes" params="[offset: 0, max: 10]">Ver evaluaciones pendientes</g:link></li>
				</sec:ifAllGranted>
				<sec:ifAllGranted roles="${Role.ROLE_REVISOR}">
					<li><g:link controller="texto" action="verTextosRevision">Ver textos pendientes de revisión</g:link></li>
				</sec:ifAllGranted>
				<sec:ifAllGranted roles="${Role.ROLE_ADMIN}">
					<hr />
					<h3>Gestión de la información</h3>
					<li><g:link controller="texto" action="index">Texto</g:link></li>
					<li><g:link controller="evaluacion" action="index">Evaluación</g:link></li>
					<li><g:link controller="estudiante" action="index">Estudiante</g:link></li>
					<li><g:link controller="matrizCalificacion" action="index">Matriz</g:link></li>
					<li><g:link controller="criterio" action="index">Criterios</g:link></li>
					<li><g:link controller="respuesta" action="index">Respuestas</g:link></li>
					<li><g:link controller="opcion" action="index">Opciones de respuesta</g:link></li>
				</sec:ifAllGranted>
			</ul>
		</sec:ifLoggedIn>
	</body>
</html>
