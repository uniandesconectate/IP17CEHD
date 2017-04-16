package co.edu.uniandes.centroespanol

class Criterio {
	String nombre
	int posicion
	
	static hasMany = [opciones: OpcionCriterio]
	static belongsTo = [matrizCalificacion: MatrizCalificacion]
	
    static constraints = {
		nombre blank: false, size: 1..5000
		posicion min: 0, unique: 'matrizCalificacion'
    }
	
	String toString() {
		return nombre
	}
}
