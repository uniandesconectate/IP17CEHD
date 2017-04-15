package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.User

class Evaluacion {
	final static String ESTADO_PENDIENTE = 'Pendiente'
	final static String ESTADO_TERMINADA = 'Terminada'
	
	String estado = ESTADO_PENDIENTE
	int numeroRechazos = 0
	User evaluador
	MatrizCalificacion matrizCalificacion
	
	static hasOne = [respuestaMatrizCalificacion: RespuestaMatrizCalificacion]
	static belongsTo = [texto: Texto]
	
    static constraints = {
		estado inList: [ESTADO_PENDIENTE, ESTADO_TERMINADA]
		numeroRechazos min: 0
		respuestaMatrizCalificacion nullable: true	
    }
}
