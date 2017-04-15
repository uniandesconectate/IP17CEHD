package co.edu.uniandes.centroespanol

class RespuestaMatrizCalificacion {
	static hasMany = [respuestas: OpcionCriterio]
	static belongsTo = [evaluacion: Evaluacion]
	
    static constraints = {
    }
}
