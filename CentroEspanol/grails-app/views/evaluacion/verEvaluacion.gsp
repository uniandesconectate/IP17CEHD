<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Evaluando texto #${evaluacion.texto.id} - ${evaluacion.texto.estudiante}</h3>
		<g:form name="evaluacion" action="evaluarTexto" id="${evaluacion.id}">
			<div class="row">
				<div class="col-md-6 col-sm-12">
					<h4>Texto</h4>
					<p>${evaluacion.texto.texto}</p>
				</div>
				<div class="col-md-6 col-sm-12">
					<h4>Matriz de calificación</h4>
					<table class="table">
						<g:each var="criterio" in="${evaluacion.matrizCalificacion.criterios.sort{it.posicion}}">
							<tr>
								<td><p><strong>${criterio}</strong></p></td>
								<g:each var="opcion" in="${criterio.opciones.sort{it.numero}}">
									<td>
										<p style="text-align: center;"><strong>${opcion.numero}</strong></p>
										<p>${opcion.descripcion}</p>
										<g:radio name="criterio-${criterio.id}" value="${opcion.id}" checked="true" style="display: block; margin: auto;"/>
									</td>
								</g:each>
							</tr>
						</g:each>
					</table>
				</div>
			</div>
			<input type="submit" value="Enviar evaluación" class="btn btn-default">
		</g:form>
	</body>
</html>