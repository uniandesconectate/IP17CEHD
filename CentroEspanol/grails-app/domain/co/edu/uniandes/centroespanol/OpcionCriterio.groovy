package co.edu.uniandes.centroespanol

class OpcionCriterio {
	int numero
	String descripcion
	String retroalimentacion
	
	static belongsTo = [criterio: Criterio]

    static constraints = {
		numero min: 0
		descripcion blank: false
		retroalimentacion blank: false
    }
	
	String toString() {
		return descripcion
	}
}
