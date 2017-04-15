import javax.management.relation.RoleInfo;

import co.edu.uniandes.login.*
import co.edu.uniandes.centroespanol.*

class BootStrap {

    def init = { servletContext ->
		CentroEspanol centroEspanol = new CentroEspanol()
		def usuarios = crearUsuarios(centroEspanol)
		def matricesCalificacion = crearMatricesCalificacion(centroEspanol)
		def criterios = crearCriterios(matricesCalificacion)
		def opciones = crearOpcionesCriterio(criterios)
    }
	
    def destroy = {
		/**
		 * Á \u00C1
		 * á \u00E1
		 * É \u00C9
		 * é \u00E9
		 * Í \u00CD
		 * í \u00ED
		 * Ó \u00D3
		 * ó \u00F3
		 * Ú \u00DA
		 * ú \u00FA
		 * Ü \u00DC
		 * ü \u00FC
		 * ñ \u00F1
		 * Ñ \u00D0
		 * ¿ \u00BF
		 * @return
		 */
    }
	
	def crearUsuarios(CentroEspanol centroEspanol) {
		def usuarios = []
		
		usuarios.add(new User(email: 'soportelidie@uniandes.edu.co', username: 'admin', password: '12345', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'cr.calle@uniandes.edu.co', username: 'cr.calle1', password: '12345', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'gcortes@uniandes.edu.co', username: 'gcortes1', password: '12345', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'se-busto@uniandes.edu.co', username: 'se-busto1', password: '12345', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'cf.agudelo12@uniandes.edu.co', username: 'cf.agudelo1', password: '12345', centroEspanol: centroEspanol))
		
		for(usuario in usuarios) {
			centroEspanol.addToUsuarios(usuario)
		}
		
		Role adminRol = new Role(authority: Role.ROLE_ADMIN, centroEspanol: centroEspanol)
		centroEspanol.addToRoles(adminRol)
		
		centroEspanol.save(flush: true)
		
		for(usuario in usuarios) {
			UserRole.create(usuario, adminRol)
		}
		
		for(int i = 1; i <= 5; i++) {
			User usuario = new User(email: "uprueba${i}@uniandes.edu.co", username: "uprueba${i}", password: '12345', centroEspanol: centroEspanol)
			centroEspanol.addToUsuarios(usuario)
			usuarios.add(usuario)
		}
		
		def autoridades = [Role.ROLE_ESTUDIANTE, Role.ROLE_EVALUADOR, Role.ROLE_REVISOR]
		def roles = []
		for(authority in autoridades) {
			Role rol = new Role(authority: authority, centroEspanol: centroEspanol)
			centroEspanol.addToRoles(rol)
			roles.add(rol)
		}
		
		centroEspanol.save(flush: true)
		
		for(usuario in usuarios) {
			for(rol in roles) {
				UserRole.create(usuario, rol)
			}
		}
		
		return usuarios
	}
	
	def crearMatricesCalificacion(CentroEspanol centroEspanol) {
		def matricesCalificacion = []
		
		MatrizCalificacion matrizCalificacion = new MatrizCalificacion(nombre: 'Matriz de prueba', centroEspanol: centroEspanol)
		centroEspanol.addToMatricesCalificacion(matrizCalificacion)
		matricesCalificacion.add(matrizCalificacion)

		return matricesCalificacion		
	}
	
	def crearCriterios(matricesCalificacion) {
		def criterios = []
		
		for(matrizCalificacion in matricesCalificacion) {
			for(int i = 1; i <= 5; i++) {
				Criterio criterio = new Criterio(nombre: "Criterio ${i}", posicion: i, matrizCalificacion: matrizCalificacion)
				matrizCalificacion.addToCriterios(criterio)
				criterios.add(criterio)
			}
		}
		
		return criterios
	}
	
	def crearOpcionesCriterio(criterios) {
		def opciones = []
		
		for(criterio in criterios) {
			for(int i = 1; i <= 5; i++) {
				OpcionCriterio opcion = new OpcionCriterio(numero: i, descripcion: "Opcion ${i}", retroalimentacion: "Retroalimentacion ${i}")
				criterio.addToOpciones(opcion)
				opciones.add(opcion)
			}
		}
		
		return opciones
	}
}
