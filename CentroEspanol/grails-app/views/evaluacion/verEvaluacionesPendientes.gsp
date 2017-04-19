<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Evaluaciones pendientes</h3>
		<g:if test="${evaluaciones.isEmpty()}">
			<p>No se encontraron evaluaciones pendientes</p>
		</g:if>
		<div class="list-group">
			<g:each var="evaluacion" in="${evaluaciones}">
				<g:link action="verEvaluacion" id="${evaluacion.id}" class="list-group-item">Texto #${evaluacion.texto.id} - ${evaluacion.texto.estudiante}</g:link>
			</g:each>
		</div>
		<g:paginate action="verEvaluacionesPendientes" total="${total}"/>
	</body>
</html>