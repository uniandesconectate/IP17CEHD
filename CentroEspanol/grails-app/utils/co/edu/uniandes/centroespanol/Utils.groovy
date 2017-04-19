package co.edu.uniandes.centroespanol

class Utils {
	static double calcularPromedioRespuesta(RespuestaMatrizCalificacion respuestaMatrizCalificacion) {
		int suma = 0
		for(respuesta in respuestaMatrizCalificacion.respuestas) {
			suma += respuesta.numero
		}
		return suma/respuestaMatrizCalificacion.respuestas.size()
	}
}
