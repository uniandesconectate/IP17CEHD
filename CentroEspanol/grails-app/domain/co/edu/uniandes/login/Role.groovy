package co.edu.uniandes.login

import co.edu.uniandes.centroespanol.CentroEspanol

class Role {
	final static String ROLE_ADMIN = 'ROLE_ADMIN'
	final static String ROLE_EVALUADOR = 'ROLE_EVALUADOR'
	final static String ROLE_REVISOR = 'ROLE_REVISOR'
	final static String ROLE_ESTUDIANTE = 'ROLE_ESTUDIANTE'
	
	String authority
	
	static belongsTo = [centroEspanol: CentroEspanol]

	static mapping = {
		cache true
	}

	static constraints = {
		authority blank: false, unique: true, inList: [ROLE_ADMIN, ROLE_ESTUDIANTE, ROLE_EVALUADOR, ROLE_REVISOR]
	}
	
	String toString() {
		return authority
	}
}
