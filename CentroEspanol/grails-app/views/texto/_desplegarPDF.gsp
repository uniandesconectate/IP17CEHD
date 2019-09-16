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
				DIAGNÓSTICO DE ESCRITURA PARA EL CONTEXTO UNIVERSITARIO<br/>
			</strong>
		</center>
		<br />
		<p>Estimado(a) ${texto.estudiante.nombre}:</p>
		<br />
		<p style="text-align:justify">La escritura es una herramienta fundamental en el contexto académico y profesional. El Centro de Español le ofrece, de manera gratuita, acompañamiento para desarrollar y fortalecer sus habilidades comunicativas, con el fin de enfrentar exitosamente los retos académicos durante sus estudios en ${texto.estudiante.programa.encodeAsHTML()}, así como en su vida profesional.</p>
		<p style="text-align:justify">A continuación, encontrará un reporte que muestra un diagnóstico de sus habilidades de escritura. En este usted encontrará la descripción de su nivel de desempeño actual en la construcción de oraciones y de párrafos; en la presentación coherente de sus ideas en el texto; en el uso de la ortografía y el lenguaje académico; en el reconocimiento de la propiedad intelectual; en el uso de fuentes para argumentar y en la construcción tanto de una postura como de argumentos que la sustentan. Del mismo modo, usted hallará el desempeño excelente con respecto a estos criterios.</p>
		<br />
		<table style="border: 1px solid gray; border-collapse: collapse;" >
			<tr>
				<th colspan="3" style="border: 1px solid gray; background-color: #bce8f1;"><center>DIAGNÓSTICO DE ESCRITURA PARA EL CONTEXTO UNIVERSITARIO</center></th>
			</tr>
			<tr>
				<th style="border: 1px solid gray; background-color: #bce8f1;"><center>Criterio</center></th>
				<th style="border: 1px solid gray; background-color: #bce8f1;"><center>Desempeño actual</center></th>
				<th style="border: 1px solid gray; background-color: #bce8f1;"><center>Desempeño excelente</center></th>
			</tr>
			<g:each in="${texto.evaluaciones[0].respuestaMatrizCalificacion.respuestas}" var="respuesta" stauts="i">
				<tr>
					<td style="border: 1px solid gray; padding: 5px;">${respuesta.criterio.nombre}</td>
					<td style="border: 1px solid gray; padding: 5px;">${respuesta.retroalimentacion}</td>
					<td style="border: 1px solid gray; padding: 5px;">${respuesta.criterio.opciones.sort(false){-it.numero}.first()}</td>
				</tr>
				<div class="row">
					
				</div>
			</g:each>
		</table>
		
		<br />
		<br />
		<p style="text-align:justify">Para recibir retroalimentación personalizada acerca de este reporte por parte de un tutor del Centro, pida una cita a través del siguiente enlace electrónico: <a href="https://centrodeespanol.uniandes.edu.co/">https://centrodeespanol.uniandes.edu.co/</a>.</p>
		<p style="text-align:justify">En esta tutoría usted podrá interactuar con un tutor a partir de su texto para desarrollar habilidades que le permitan pensar críticamente y usar la escritura como medio para comunicar sus ideas. Además, usted también podrá desarrollar sus habilidades de lectura y de comunicación oral para distintos ejercicios académicos en este tipo de interacciones con un(a) tutor(a).</p>
		<p style="text-align:justify">Lo esperamos.</p>
		<p style="text-align:justify">Cordial saludo,</p>
		<br />
		<strong>Centro de Español</strong> <br />
		Universidad de los Andes <br />
		Tels. 3394949 ext. 4927 <br />
		ejerciciodeescritura@uniandes.edu.co <br />
	</body>
</html>