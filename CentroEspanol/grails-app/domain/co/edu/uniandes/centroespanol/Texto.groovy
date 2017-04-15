package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.User

class Texto {
	static final String ESTADO_PENDIENTE = "Pendiente"
	static final String ESTADO_REVISADO = "Revisado"
	
	String texto
	String estado = ESTADO_PENDIENTE
	Estudiante estudiante
	User revisor
	
	static hasMany = [evaluaciones: Evaluacion]
	static belongsTo = [centroEspanol: CentroEspanol]
	
    static constraints = {
		texto blank: false, size: 1..14000
		evaluaciones minSize: 2
		estado inList: [ESTADO_PENDIENTE, ESTADO_REVISADO]
    }
}
