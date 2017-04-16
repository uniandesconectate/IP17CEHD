package co.edu.uniandes.centroespanol

class OpcionCriterio {
	int numero
	String nivel
	String descripcion
	String retroalimentacion
	
	static belongsTo = [criterio: Criterio]

    static constraints = {
		numero min: 0
		nivel black: false, size: 1..100
		descripcion blank: false, size: 1..5000
		retroalimentacion blank: false, size: 1..5000
    }
	
	String toString() {
		return descripcion
	}
}
