package co.edu.uniandes.centroespanol

class MatrizCalificacion {
	String nombre
	
	static hasMany = [criterios: Criterio]
	static belongsTo = [centroEspanol: CentroEspanol]
	
    static constraints = {
		nombre blank: false, unique: 'centroEspanol'
    }
	
	String toString() {
		return nombre
	}
}
