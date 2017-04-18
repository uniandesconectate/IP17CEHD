<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
	<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
   		<meta name="layout" content="main"/>
		<title>Evaluación ${texto.estudiante.usuario}</title>
	</head>
	<body>
		<rendering:inlineJpeg bytes="${bytesImagen}" style="width: 100%;"/>
		<center>
			<strong>
				CENTRO DE ESPAÑOL <br/>
				UNIVERSIDAD DE LOS ANDES <br/>
				EJERCICIO DE ESCRITURA <br/>
				RETROALIMENTACIÓN PARA ESTUDIANTES <br/>
			</strong>
		</center>
		<p>Estimado(a) ${texto.estudiante.nombre}:</p>
		<p style="text-align:justify">Nos alegra mucho haber leído su ejercicio de escritura. Su atención a nuestra invitación indica su compromiso académico y es una oportunidad para conocernos. El <a href="https://programadeescritura.uniandes.edu.co">Centro de Español</a> le ofrece, de manera gratuita, acompañamiento para desarrollar y fortalecer sus competencias de escritura y así enfrentar exitosamente los retos académicos durante sus estudios en 
		${texto.estudiante.programa.encodeAsHTML()}</p>
		<p style="text-align:justify">A continuación encontrará dos tablas que muestran una valoración de su ejercicio de escritura. La primera le ofrece retroalimentación en torno a tres competencias de escritura definidas por el Centro de Español como competencias fundamentales en el ejercicio de escritura: la analítica, la reflexiva y la argumentativa. Para cada una de estas usted encontrará, en la primera columna, su nivel de desarrollo actual y, en la segunda columna, el nivel de desarrollo definido como excelente.</p>
		<p style="text-align:justify">En la segunda tabla usted encontrará una representación gráfica de todos los criterios definidos para cada una de las competencias. A partir de la valoración de su ejercicio de escritura, señalamos con una X sus desempeños de manera que usted pueda identificar gráficamente su nivel actual de desarrollo de las competencias mencionadas. De este modo, podrá identificar los desempeños que usted puede fortalecer por medio de las tutorías que se ofrecen en el Centro de Español.</p>
		
		<center><strong>Tabla 1.</strong></center>
		<table style="border: 1px solid gray">
			<tr>
				<th style="border: 1px solid gray">Competencia</th>
				<th style="border: 1px solid gray">Desempeño actual</th>
				<th style="border: 1px solid gray">Desempeño excelente</th>
			</tr>
			<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas}" var="respuesta" stauts="i">
				<tr>
					<td style="border: 1px solid gray">${respuesta.criterio.nombre}</td>
					<td style="border: 1px solid gray">${respuesta.retroalimentacion}</td>
					<td style="border: 1px solid gray">${respuesta.criterio.opciones.sort(false){-it.numero}.first()}</td>
				</tr>
				<div class="row">
					
				</div>
			</g:each>
		</table>
		
		<br />
		<center><strong>Tabla 2.</strong></center>
		<table style="border: 1px solid gray">
			<tr>
				<th style="border: 1px solid gray">Competencias</th>
				<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas[0].criterio.opciones.sort(false){-it.numero}}" var="opcion" status="i">
					<th style="border: 1px solid gray">${opcion.nivel}</th>	
				</g:each>
			</tr>
			<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas}" var="respuesta" stauts="i">
				<tr>
					<td style="border: 1px solid gray">${respuesta.criterio.nombre}</td>
					<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas[0].criterio.opciones.sort(false){-it.numero}}" var="opcion" status="i">
						<td style="border: 1px solid gray">
							<g:if test="${opcion.nivel==respuesta.nivel}">
								X
							</g:if>
						</td>
					</g:each>
				</tr>
				<div class="row">
					
				</div>
			</g:each>
		</table>
		<br/>
		<p style="text-align:justify">Para recibir retroalimentación personalizada por parte de un tutor del Centro pida una cita entrando a la página <a href="https://programadeescritura.uniandes.edu.co/index.php/centro-de-escritura">https://programadeescritura.uniandes.edu.co/index.php/centro-de-escritura</a>.</p>
		<p style="text-align:justify">En esta conversación con el tutor, usted podrá reconocer cómo mejorar éste y todos sus textos académicos para lograr así un buen desempeño en la Universidad. En el Centro le ofrecemos estrategias para desarrollar competencias de lectura y escritura académica.</p>
		<p style="text-align:justify">Lo esperamos. Cordial saludo,</p>
		Centro de Español <br />
		Universidad de los Andes <br />
		Tels. 3394949 ext. 4926 <br />
		ejerciciodeescritura@uniandes.edu.co <br />
	</body>
</html>