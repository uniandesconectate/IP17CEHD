<html>
	<head>
		<meta name="layout" content="main"/>
	</head>
	<body>
		<h3>Subir información</h3>
		<div id="formulario">
			<g:uploadForm name="form" action="cargarInformacion">
				<div class="form-group">
			  		<label for="matrizID">Matriz de calificación a utilizar:</label>
				  	<g:select name="matrizID" from="${matricesCalificacion}" optionKey="id" class="form-control"/>
				</div>
				<input type="file" name="archivo" class="btn btn-default"/>
				<br>
				<input type="submit" value="Subir archivo" onclick="subirInformacion()" class="btn btn-default"/> 
			</g:uploadForm>
		</div>
		<div id="carga" style="display: none;">
			<asset:image src="spinner.gif"/>
		</div>
	</body>
</html>