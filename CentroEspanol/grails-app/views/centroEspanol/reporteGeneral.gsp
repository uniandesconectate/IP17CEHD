<html>
	<head>
		<meta name="layout" content="main"/>
		<style type="text/css">
		    table, th, td{
		        border: 1px solid #666;
		    }
		    table th, table td{
		        padding: 10px; /* Apply cell padding */
		    }
		</style>
	</head>
	<body>
		<h3>Reporte general</h3>
		<div id="formulario">
			<g:form name="resultadoFinal" action="reporteGeneral" id="reporteGeneral">
				<div class="form-group">
			  		<label for="inicio">Texto inicial para mostrar: </label>
			  		<g:field type="number" name="inicio" min="1" required="true" value="${inicio==null?0:inicio}"> </g:field>
				</div>
				<div class="form-group">
			  		<label for="fin">Texto final para mostrar</label>
			  		<g:field type="number" name="fin" min="1" required="true" value="${fin==null?0:fin}"> </g:field>
				</div>
				<div class="form-group">
			  		<label for="separador">Separador para descarga</label>
			  		<g:select name="separador" id="separador" from="[',',';']" />
				</div>
				<g:actionSubmit value="Ver reporte" action="reporteGeneral"/>
				<g:actionSubmit value="Ver reporte en Csv" action="reporteGeneralCsv"/>
			</g:form>
		</div>
		<hr />
		<div class="container">
			<table border="1" cellspacing="10" cellpadding="10">
				<thead>
					<th><center>Texto</center></th>
					<th>Código</th>
					<th>Usuario</th>
					<th>Programa</th>
					<th>Evaluador</th>
					<th>Devoluciones</th>
					<th>Estado</th>
					<g:if test="${textos.size()>0}">
						<g:each in="${matrizInicial.criterios}" var="criterio" status="i">
							<th>${criterio.nombre}</th>
						</g:each>
					</g:if>
					<th>Promedio</th>
				</thead>
				<tbody>
					<g:each in="${textos}" var="texto" status="i">
						<g:each in="${texto.evaluaciones}" var="evaluacion" status="j">
							<g:set var="average" value="${0}"/>
							<tr>
								<td>${texto.id}</td>
								<td>${texto.estudiante.codigo}</td>
								<td>${texto.estudiante.usuario.username}</td>
								<td>${texto.estudiante.programa}</td>
								<td>${evaluacion.evaluador.username}</td>
								<td><center>${evaluacion.numeroRechazos}</center></td>
								<td>${evaluacion.estado}</td>
								<g:if test="${evaluacion.respuestaMatrizCalificacion!=null}">
									<g:each in="${evaluacion.respuestaMatrizCalificacion.respuestas}" var="respuesta" status="k">
										<td><center>${respuesta.numero}</center></td>
										<g:set var="average" value="${average + (respuesta.numero/evaluacion.respuestaMatrizCalificacion.respuestas.size())}" />
									</g:each>
									<td><center><g:formatNumber number="${average}" format="##0.##" /></center></td>
								</g:if>
							</tr>
						</g:each>
					</g:each>
				</tbody>
			</table>
		</div>
	</body>
</html>