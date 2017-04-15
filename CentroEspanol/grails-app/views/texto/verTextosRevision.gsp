<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Textos pendientes de revision</h3>
		<g:if test="${textos.isEmpty()}">
			<p>No se encontraron textos pendientes de revisión</p>
		</g:if>
		<div class="list-group">
			<g:each var="texto" in="${textos}">
				<g:link action="verTexto" id="${texto.id}" class="list-group-item">Texto #${texto.id} - ${texto.estudiante}</g:link>
			</g:each>
		</div>
	</body>
</html>