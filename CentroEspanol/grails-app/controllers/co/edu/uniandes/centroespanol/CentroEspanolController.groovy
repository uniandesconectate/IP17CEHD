package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.*
import org.springframework.web.multipart.MultipartFile
import grails.plugin.springsecurity.annotation.Secured
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@Secured(['ROLE_ADMIN'])
class CentroEspanolController {

	static scaffold = true
	
	def springSecurityService
	def grailsApplication
	
	def subirInformacion() {
		User usuario = springSecurityService.getCurrentUser()
		[matricesCalificacion: usuario.centroEspanol.matricesCalificacion]
	}
	
	def cargarInformacion() {
		CentroEspanol centroEspanol = springSecurityService.getCurrentUser().centroEspanol
		MatrizCalificacion matrizCalificacion = MatrizCalificacion.get(params.matrizID)
		if(matrizCalificacion) {
			CentroEspanol.withTransaction { status ->
				try {
					MultipartFile archivo = request.getFile('archivo')
					def split
					if(archivo&&(split = archivo.getOriginalFilename().split('\\.')).length>1&&split[split.length-1]=='xlsx') {
						File archivoLocal = new File(grailsApplication.config.co.edu.uniandes.uploadfolder + archivo.getOriginalFilename())
						archivo.transferTo(archivoLocal)
						FileInputStream fis = new FileInputStream(archivoLocal)
						Workbook workbook = new XSSFWorkbook(fis)
						Sheet firstSheet = workbook.getSheetAt(0)
						Iterator<Row> iterator = firstSheet.iterator()
						int fila = 0
						while(iterator.hasNext()) {
							Row nextRow = iterator.next()
							if(fila != 0) {
								leerFila(centroEspanol, matrizCalificacion, nextRow, fila)
							}
							fila++
						}
					} else {
						throw new Exception('No se seleccion\u00F3 un archivo de Excel v\u00E1lido (.xlsx)')
					}
					flash.message = 'La informaci\u00F3n fue cargada correctamente'
				} catch(Exception e) {
					status.setRollbackOnly()
					flash.error = e.getMessage()
				}
			}
		} else {
			flash.error = 'No se encontr\u00F3 la matriz de calificaci\u00F3n seleccionada'
		}
		redirect(action: 'subirInformacion')
	}
	
	private void leerFila(CentroEspanol centroEspanol, MatrizCalificacion matrizCalificacion, Row row, int fila) {
		Iterator<Cell> cellIterator = row.cellIterator()
		int columna = 0
		Integer codigo = null
		Integer documento = null
		String nombre = null
		String apellido = null
		String programa = null
		String correo = null
		String texto = null
		String evaluador1 = null
		String evaluador2 = null
		String revisor = null
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next()
			if(columna == 0 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				codigo = cell.getNumericCellValue()
			} else if(columna == 1 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				documento = cell.getNumericCellValue()
			} else if(columna == 2) {
				nombre = cell.getStringCellValue()
			} else if(columna == 3) {
				apellido = cell.getStringCellValue()
			} else if(columna == 4) {
				programa = cell.getStringCellValue()
			} else if(columna == 5) {
				correo = cell.getStringCellValue()
			} else if(columna == 6) {
				texto = cell.getStringCellValue()
				texto = texto.replaceAll('\\r', '\n')
			} else if(columna == 7) {
				evaluador1 = cell.getStringCellValue()
			} else if(columna == 8) {
				evaluador2 = cell.getStringCellValue()
			} else if(columna == 9) {
				revisor = cell.getStringCellValue()
			} else {
				throw new Exception("La fila ${fila} tiene mas columnas de las debidas")
			}
			columna++
		}
		cargarFila(centroEspanol, matrizCalificacion, codigo, documento, nombre, apellido, programa, correo, texto, evaluador1, evaluador2, revisor, fila)
	}
	
	private void cargarFila(CentroEspanol centroEspanol, MatrizCalificacion matrizCalificacion, Integer codigo, Integer documento, String nombre, String apellido,
							String programa, String correo, String texto, String evaluador1, String evaluador2, String revisor, int fila) {
		if(codigo&&documento&&nombre&&apellido&&programa&&correo&&texto&&evaluador1&&evaluador2&&revisor) {
			Role rolEstudiante = Role.findByAuthority(Role.ROLE_ESTUDIANTE)
			Role rolEvaluador = Role.findByAuthority(Role.ROLE_EVALUADOR)
			Role rolRevisor = Role.findByAuthority(Role.ROLE_REVISOR)
			def split = correo.split('@')
			if(split.length == 2) {
				String username = split[0]
				User usuarioEstudiante = User.findByUsername(username)
				if(!usuarioEstudiante) {
					usuarioEstudiante = new User(email: correo, username: username, password: 'NZ2fWLIMjzPuUQA6Ut_A', centroEspanol: centroEspanol)
					if(usuarioEstudiante.validate()) {
						centroEspanol.addToUsuarios(usuarioEstudiante).save(flush: true)
					} else {
						throw new Exception("No se pudo crear el usuario del estudiante en la fila ${fila}")
					}
				}
				if(!UserRole.exists(usuarioEstudiante.id, rolEstudiante.id)) {
					UserRole.create(usuarioEstudiante, rolEstudiante)
				}
				Estudiante estudiante = Estudiante.findByUsuario(usuarioEstudiante)
				if(!estudiante) {
					estudiante = new Estudiante(usuario: usuarioEstudiante, codigo: codigo, documento: documento, nombre: nombre,
					apellido: apellido, programa: programa, centroEspanol: centroEspanol)
					if(estudiante.validate()) {
						centroEspanol.addToEstudiantes(estudiante).save(flush: true)
					} else {
						throw new Exception("No se pudo crear el estudiante en la fila ${fila}")
					}
				}
				User usuarioRevisor = User.findByUsername(revisor)
				if(!usuarioRevisor) {
					usuarioRevisor = new User(email: "${revisor}@uniandes.edu.co", username: revisor, password: 'NZ2fWLIMjzPuUQA6Ut_b', centroEspanol: centroEspanol)
					if(usuarioRevisor.validate()) {
						centroEspanol.addToUsuarios(usuarioRevisor).save(flush: true)
					} else {
						throw new Exception("No se pudo crear el usuario revisor en la fila ${fila}")
					}
				}
				if(!UserRole.exists(usuarioRevisor.id, rolRevisor.id)) {
					UserRole.create(usuarioRevisor, rolRevisor)
				}
				User usuarioEvaluador1 = User.findByUsername(evaluador1)
				if(!usuarioEvaluador1) {
					usuarioEvaluador1 = new User(email: "${evaluador1}@uniandes.edu.co", username: evaluador1, password: 'NZ2fWLIMjzPuUQA6Ut_c', centroEspanol: centroEspanol)
					if(usuarioEvaluador1.validate()) {
						centroEspanol.addToUsuarios(usuarioEvaluador1).save(flush: true)
					} else {
						throw new Exception("No se pudo crear el usuario evaluador 1 en la fila ${fila}")
					}
				}
				if(!UserRole.exists(usuarioEvaluador1.id, rolEvaluador.id)) {
					UserRole.create(usuarioEvaluador1, rolEvaluador)
				}
				User usuarioEvaluador2 = User.findByUsername(evaluador2)
				if(!usuarioEvaluador2) {
					usuarioEvaluador2 = new User(email: "${evaluador2}@uniandes.edu.co", username: evaluador2, password: '12345', centroEspanol: centroEspanol)
					if(usuarioEvaluador2.validate()) {
						centroEspanol.addToUsuarios(usuarioEvaluador2).save(flush: true)
					} else {
						throw new Exception("No se pudo crear el usuario evaluador 2 en la fila ${fila}")
					}
				}
				if(!UserRole.exists(usuarioEvaluador2.id, rolEvaluador.id)) {
					UserRole.create(usuarioEvaluador2, rolEvaluador)
				}
				Texto textoEstudiante = new Texto(texto: texto, estudiante: estudiante, revisor: usuarioRevisor, centroEspanol: centroEspanol)
				if(textoEstudiante.validate()) {
					crearEvaluacion(usuarioEvaluador1, textoEstudiante, matrizCalificacion, fila)
					crearEvaluacion(usuarioEvaluador2, textoEstudiante, matrizCalificacion, fila)
					centroEspanol.addToTextos(textoEstudiante).save(flush: true)
				} else {
					throw new Exception("No se pudo crear el texto en la fila ${fila}")
				}
			} else {
				throw new Exception("El correo en la fila ${fila} es inv\u00E1lido")
			}
		} else {
			throw new Exception("No se encontr\u00F3 algunos de los campos requeridos en la fila ${fila}")
		}
	}

	private void crearEvaluacion(User evaluador, Texto texto, MatrizCalificacion matrizCalificacion, int fila) {
		Evaluacion evaluacion = new Evaluacion(evaluador: evaluador, matrizCalificacion: matrizCalificacion, texto: texto)
		if(evaluacion.validate()) {
			texto.addToEvaluaciones(evaluacion)
		} else {
			throw new Exception("No se pudo asignar la evaluaci\u00F3n del evaluador 1 al texto en la fila ${fila}")
		}
	}
}
