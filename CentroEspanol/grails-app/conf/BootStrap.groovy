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
		
		usuarios.add(new User(email: 'soportelidie@uniandes.edu.co', username: 'admin', password: 'NZ2fWLIMjzPuUQA6UtRk', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'cr.calle@uniandes.edu.co', username: 'cr.calle', password: 'NZ2fWLIMjzPuUQA6UtRk', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'gcortes@uniandes.edu.co', username: 'gcortes', password: 'NZ2fWLIMjzPuUQA6UtRk', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'se-busto@uniandes.edu.co', username: 'se-busto', password: 'NZ2fWLIMjzPuUQA6UtRk', centroEspanol: centroEspanol))
		usuarios.add(new User(email: 'cf.agudelo12@uniandes.edu.co', username: 'cf.agudelo12', password: 'NZ2fWLIMjzPuUQA6UtRk', centroEspanol: centroEspanol))
		
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
		
		MatrizCalificacion matrizCalificacion = new MatrizCalificacion(nombre: 'Matriz de texto argumentativo', centroEspanol: centroEspanol)
		centroEspanol.addToMatricesCalificacion(matrizCalificacion)
		matricesCalificacion.add(matrizCalificacion)

		return matricesCalificacion		
	}
	
	def crearCriterios(matricesCalificacion) {
		def criterios = []
		def criteriosNombres = ["ORACIONES: Las oraciones son estructuras gramaticales compuestas por un sujeto y un predicado (verbo y complemento).",
			"P\u00C1RRAFOS: Un p\u00E1rrafo es un conjunto de oraciones articuladas entre s\u00ED tem\u00E1ticamente.",
			"CONECTORES L\u00D3GICOS: Los conectores l\u00F3gicos permiten establecer relaciones entre ideas. Su uso variado y pertinente contribuye a la cohesi\u00F3n y coherencia del texto.",
			"COHERENCIA: Un texto es coherente cuando la relaci\u00F3n l\u00F3gica entre sus p\u00E1rrafos aporta al cumplimiento de un prop\u00F3sito determinado.",
			"ORTOGRAF\u00CDA: La ortograf\u00EDa es el conjunto de normas que regulan la correcta escritura de las palabras de una lengua. Escribir correctamente permite la claridad y la comprensi\u00F3n de lo que se quiere comunicar.",
			"USO ACAD\u00C9MICO DEL LENGUAJE: El uso cotidiano de la lengua es diferente al uso acad\u00E9mico y cada uno tiene sus propias convenciones.",
			"RECONOCIMIENTO DE LA PROPIEDAD INTELECTUAL: Es indispensable identificar cu\u00E1ndo las ideas son propias y cu\u00E1ndo son ajenas. Para esto es necesario usar consistentemente un sistema de citaci\u00F3n.",
			"USO DE FUENTES PARA LA ARGUMENTACI\u00D3N: El uso adecuado de fuentes implica conocer detalladamente el contenido de un texto escrito, confrontar ese contenido con los saberes previos, contrastar la informaci\u00F3n con otras fuentes, valorar la informaci\u00F3n nueva y relacionarla con sus ideas.",
			"ARGUMENTACI\u00D3N: Escribir un texto argumentativo implica demostrar, comprobar o convencer al lector de una tesis (afirmaci\u00F3n debatible) por medio del uso de argumentos expuestos de forma l\u00F3gica."
		]
		
		for(matrizCalificacion in matricesCalificacion) {
			for(int i = 0; i < criteriosNombres.size(); i++) {
				Criterio criterio = new Criterio(nombre: criteriosNombres[i], posicion: (i+1), matrizCalificacion: matrizCalificacion)
				matrizCalificacion.addToCriterios(criterio)
				criterios.add(criterio)
			}
		}
		
		return criterios
	}
	
	def crearOpcionesCriterio(criterios) {
		def opciones = []
		def niveles = ["Alto", "Intermedio Alto", "Intermedio", "Intermedio Bajo", "Bajo"]
		def opcionesCalificacion = []
		
		opcionesCalificacion.add("Construye oraciones con una estructura interna adecuada (sujeto, verbo, complemento).")
		opcionesCalificacion.add("La mayor\u00EDa de sus oraciones tienen una estructura interna adecuada (sujeto, verbo, complemento), pero a veces comete errores de puntuaci\u00F3n.")
		opcionesCalificacion.add("Algunas de sus oraciones tienen una estructura interna adecuada (sujeto, verbo, complemento). Sin embargo, en otras comete errores de puntuaci\u00F3n que afectan el sentido del texto.")
		opcionesCalificacion.add("La mayor\u00EDa de sus oraciones carecen de estructura interna adecuada (sujeto, verbo, complemento) porque comete muchos errores de puntuaci\u00F3n.")
		opcionesCalificacion.add("Sus oraciones tienen tantos errores de gram\u00E1tica y puntuaci\u00F3n que resultan confusas, incompletas o incomprensibles.")
		
		opcionesCalificacion.add("Cada p\u00E1rrafo desarrolla una sola idea por medio de un conjunto de oraciones articuladas entre s\u00ED tem\u00E1ticamente.")
		opcionesCalificacion.add("Cada p\u00E1rrafo desarrolla una sola idea aunque algunas de las oraciones secundarias est\u00E1n desarticuladas porque repiten informaci\u00F3n o contienen informaci\u00F3n irrelevante.")
		opcionesCalificacion.add("La mayor\u00EDa de las  veces construye p\u00E1rrafos, pero por lo menos en uno de ellos no plantea o no desarrolla una idea central, o comete errores de puntuaci\u00F3n que afectan el sentido del p\u00E1rrafo.")
		opcionesCalificacion.add("La mayor\u00EDa de las veces no construye p\u00E1rrafos porque  (i) no desarrolla una sola idea, (ii) escribe oraciones extensas en las que aborda varios temas o (iii) hace una enumeraci\u00F3n de ideas.")
		opcionesCalificacion.add("El texto no est\u00E1 compuesto por p\u00E1rrafos")
		
		opcionesCalificacion.add("La variedad y el uso pertinente de conectores l\u00F3gicos contribuyen a la cohesi\u00F3n y coherencia de las ideas.")
		opcionesCalificacion.add("Utiliza con pertinencia los conectores l\u00F3gicos, aunque algunas veces los repite.")
		opcionesCalificacion.add("Comete algunos errores en el uso de conectores l\u00F3gicos (repetici\u00F3n del mismo conector o funci\u00F3n equivocada) que afectan la cohesi\u00F3n y coherencia de las ideas.")
		opcionesCalificacion.add("Cuando usa conectores l\u00F3gicos lo hace de manera insuficiente e inadecuada.")
		opcionesCalificacion.add("No usa conectores l\u00F3gicos.")
		
		opcionesCalificacion.add("El texto es coherente porque tiene un prop\u00F3sito que se puede identificar y cada uno de los p\u00E1rrafos aporta a este de manera distinta.")
		opcionesCalificacion.add("El texto es coherente porque tiene un prop\u00F3sito que se puede identificar y cada uno de los p\u00E1rrafos aporta a este, pero algunos presentan informaci\u00F3n repetida.")
		opcionesCalificacion.add("El texto no es totalmente coherente porque alguno de sus p\u00E1rrafos aborda un tema distinto o est\u00E1 desarticulado de los otros.")
		opcionesCalificacion.add("Aunque enuncia un prop\u00F3sito, el texto no tiene coherencia porque la mayor\u00EDa de los p\u00E1rrafos no aportan a este.")
		opcionesCalificacion.add("El texto no tiene coherencia.")
		
		opcionesCalificacion.add("Usa adecuadamente la ortograf\u00EDa en todo el texto.")
		opcionesCalificacion.add("Ocasionalmente comete pocos errores ortogr\u00E1ficos")
		opcionesCalificacion.add("Frecuentemente comete los mismos errores ortogr\u00E1ficos.")
		opcionesCalificacion.add("Frecuentemente comete muchos y diferentes errores ortogr\u00E1ficos, que dificultan al lector comprender la mayor\u00EDa de las ideas.")
		opcionesCalificacion.add("Sus oraciones tienen tantos errores de ortograf\u00EDa que resultan incomprensibles.")
		
		opcionesCalificacion.add("Usa consistentemente y de manera diversa el vocabulario y los conceptos propios del \u00E1mbito acad\u00E9mico.")
		opcionesCalificacion.add("Usa el vocabulario y los conceptos propios del \u00E1mbito acad\u00E9mico pero tiende a repetir t\u00E9rminos con frecuencia.")
		opcionesCalificacion.add("Usa el vocabulario y los conceptos propios del \u00E1mbito acad\u00E9mico pero tiende a utilizarlo de manera inadecuada (exagerada o con un significado err\u00F3neo).")
		opcionesCalificacion.add("Usa un vocabulario coloquial junto con el vocabulario y los conceptos propios del \u00E1mbito acad\u00E9mico")
		opcionesCalificacion.add("Usa un vocabulario coloquial en la totalidad del texto y no presenta conceptos propios del \u00E1mbito acad\u00E9mico")
		
		opcionesCalificacion.add("Siempre que usa producci\u00F3n acad\u00E9mica ajena identifica la fuente. Utiliza de forma consistente y correcta un formato de citaci\u00F3n y referencia.")
		opcionesCalificacion.add("Siempre que usa producci\u00F3n acad\u00E9mica ajena identifica la fuente.  Usa un formato de citaci\u00F3n y referencia, pero comete algunos errores.")
		opcionesCalificacion.add("Siempre que usa producci\u00F3n acad\u00E9mica ajena identifica la fuente.  Usa un formato de citaci\u00F3n y referencia, pero comete muchos errores.")
		opcionesCalificacion.add("No usa citas de las fuentes asignadas en el texto.")
		opcionesCalificacion.add("Cuando usa producci\u00F3n acad\u00E9mica ajena no identifica la fuente (incurre en una falta disciplinaria).No usa un formato de citaci\u00F3n y referencia.")
		
		opcionesCalificacion.add("Inserta en el texto citas pertinentes para  sustentar un planteamiento. Explica la relevancia de las citas que usa. El texto es el resultado de una conversaci\u00F3n acad\u00E9mica que pone en di\u00E1logo argumentos propios y ajenos.")
		opcionesCalificacion.add("Inserta en el texto citas pertinentes aunque algunas veces no logra sustentar un planteamiento con ellas. Casi siempre explica la relevancia de las citas que usa. El texto muestra que el estudiante pone en di\u00E1logo argumentos propios y ajenos.")
		opcionesCalificacion.add("Inserta en el texto citas pertinentes pero no logra sustentar un planteamiento con ellas. No explica la relevancia de las citas que usa. El texto muestra que el estudiante expone argumentos propios y ajenos.")
		opcionesCalificacion.add("Inserta en el texto citas que no son pertinentes para sustentar un planteamiento. No explica la relevancia de las citas que usa. El estudiante no pone en di\u00E1logo argumentos propios y ajenos.")
		opcionesCalificacion.add("No inserta citas en el texto. El texto no demuestra una conversaci\u00F3n acad\u00E9mica que pone en di\u00E1logo argumentos propios y ajenos.")
		
		opcionesCalificacion.add("Plantea una tesis y construye los argumentos que la sustentan. Pone en evidencia la relaci\u00F3n que hay entre los argumentos y la tesis. El texto incluye un p\u00E1rrafo introductorio, p\u00E1rrafos en los que desarrolla cada argumento y un p\u00E1rrafo de cierre.")
		opcionesCalificacion.add("Plantea una tesis y construye los argumentos que la sustentan. Sugiere la relaci\u00F3n que hay entre los argumentos y la tesis.  El texto tiene introducci\u00F3n, desarrollo y conclusi\u00F3n.")
		opcionesCalificacion.add("Plantea una tesis y construye algunos argumentos que la sustentan. Propone una relaci\u00F3n que no es l\u00F3gica entre los argumentos y la tesis. El texto tiene introducci\u00F3n, desarrollo y conclusi\u00F3n o cierre.")
		opcionesCalificacion.add("La idea central que propone como tesis no lo es porque no es debatible. Por lo tanto, las ideas que expone no son argumentos. La estructura no corresponde a la de un texto argumentativo.")
		opcionesCalificacion.add("No plantea una idea central porque no hay una jerarqu\u00EDa de las ideas expuestas.  La estructura no corresponde a la de un texto argumentativo.")
		
		int posicion = 0
		for(criterio in criterios) {
			for(int i = 0; i < niveles.size(); i++) {
				OpcionCriterio opcion = new OpcionCriterio(numero: niveles.size()-i, nivel: niveles[i], descripcion: opcionesCalificacion[posicion], retroalimentacion: opcionesCalificacion[posicion])
				criterio.addToOpciones(opcion)
				opciones.add(opcion)
				posicion++
			}
		}
		
		return opciones
	}
}
