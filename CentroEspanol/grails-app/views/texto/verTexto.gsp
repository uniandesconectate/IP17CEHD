<%@ page import="co.edu.uniandes.centroespanol.Utils" %>
<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Revisar texto #${texto.id} - ${texto.estudiante}</h3>
		<div class="table-responsive">
			<table class="table table-striped">
				<tr>
					<th>Criterio</th>
					<g:each var="evaluacion" in="${texto.evaluaciones}">
						<th>Elección ${evaluacion.evaluador}</th>
					</g:each>
				</tr>
				<g:each var="criterio" in="${matrizCalificacion.criterios.sort{it.posicion}}">
					<tr>
						<td>
							<strong>${criterio}</strong>
						</td>
						<g:each var="evaluacion" in="${texto.evaluaciones}">
							<td>
								<!-- RCA: Esta verificación se realiza porque los textos quedaron calificados con matrices distintas. -->
								<g:if test="${evaluacion.respuestaMatrizCalificacion.respuestas.find{it.criterio.id==criterio.id}!=null}">
									<p style="text-align: center; margin-bottom: 0px;"><strong>${evaluacion.respuestaMatrizCalificacion.respuestas.find{it.criterio.id==criterio.id}.numero}</strong></p>
									${evaluacion.respuestaMatrizCalificacion.respuestas.find{it.criterio.id==criterio.id}}
								</g:if>
							</td>
						</g:each>
					</tr>
				</g:each>
				<tr>
					<td><strong>Promedio</strong></td>
					<g:each var="evaluacion" in="${texto.evaluaciones}">
						<td style="text-align: center;"><strong><g:formatNumber number="${Utils.calcularPromedioRespuesta(evaluacion.respuestaMatrizCalificacion)}" format="###.##"/></strong></td>
					</g:each>
				</tr>
				<tr>
					<td>
						<g:link action="rechazarEvaluacionTexto" id="${texto.id}" class="btn btn-default">
							Rechazar evaluaciones
						</g:link>
						<g:link action="aceptarTexto" id="${texto.id}" class="btn btn-default">
							Terminar revisión
						</g:link>
					</td>
					<g:each var="evaluacion" in="${texto.evaluaciones}">
						<td>
							<g:link controller="evaluacion" action="rechazarEvaluacion" id="${evaluacion.id}" class="btn btn-default">
								Rechazar evaluación
							</g:link>
						</td>
					</g:each>
				</tr>
			</table>
		</div>
		<h4>Texto</h4>
		<p style="text-align:justify">${raw(texto.texto)}</p>
	</body>
</html>