package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.*

class CentroEspanol {
	static hasMany = [usuarios: User, roles: Role, estudiantes: Estudiante, textos: Texto, matricesCalificacion: MatrizCalificacion]

    static constraints = {
    }
}
