package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.User

class Estudiante {
	User usuario
	int codigo
	int documento
	String nombre
	String apellido
	String programa
	
	static belongsTo = [centroEspanol: CentroEspanol]
	
    static constraints = {
		codigo blank: false
		documento blank: false
		nombre blank: false
		apellido blank: false
		programa blank: false
    }
	
	String toString() {
		return "${nombre} ${apellido} (${usuario})"
	}
}
