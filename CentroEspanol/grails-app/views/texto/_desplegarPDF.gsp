<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Resultados de la plantilla</h3>
		<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas}" var="respuesta" stauts="i">
			<div class="row">
				${respuesta.numero} - ${respuesta.retroalimentacion}
			</div>
		</g:each>
	</body>
</html>