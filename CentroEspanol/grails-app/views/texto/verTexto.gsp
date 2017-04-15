<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Revisar texto #${texto.id} - ${texto.estudiante}</h3>
		<div class="row">
			<g:each var="evaluacion" in="${texto.evaluaciones}">
				<div class="col-sm-12 col-md-${12/texto.evaluaciones.size()}">
					<div class="panel panel-default">
					  	<div class="panel-heading">
					  		<h3 class="panel-title">Evaluacion de ${evaluacion.evaluador}</h3>
					  	</div>
					  	<div class="panel-body">
						    <table class="table">
								<thead>
									<tr>
										<th>Criterio</th>
										<th>Opción elegida</th>
									</tr>
								</thead>
								<tbody>
									<g:each var="respuesta" in="${evaluacion.respuestaMatrizCalificacion.respuestas.sort{it.criterio.posicion}}">
										<tr>
											<td>${respuesta.criterio}</td>
											<td>
												<p><strong>${respuesta.numero}</strong></p>
												<p>${respuesta.descripcion}</p>
											</td>
										</tr>
									</g:each>
								</tbody>
							</table>
					  	</div>
					</div>
					<g:link controller="evaluacion" action="rechazarEvaluacion" id="${evaluacion.id}" class="btn btn-default">
						Rechazar evaluación
					</g:link>
				</div>
			</g:each>
			<g:link controller="texto" action="rechazarEvaluacionTexto" id="${texto.id}" class="btn btn-default">
				Rechazar todas las evaluaciones
			</g:link>
			<g:link controller="texto" action="aceptarTexto" id="${texto.id}" class="btn btn-default">
				Terminar revisión y generar PDF
			</g:link>
		</div>
		<h4>Texto</h4>
		<p>${texto.texto}</p>
	</body>
</html>