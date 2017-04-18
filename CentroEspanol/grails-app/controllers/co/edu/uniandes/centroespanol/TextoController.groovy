package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.*
import grails.plugin.rendering.RenderingService
import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.io.IOUtils
import org.springframework.core.io.Resource

@Secured(['ROLE_ADMIN'])
class TextoController {

    static scaffold = true
	
	def springSecurityService
	def grailsApplication
	def assetResourceLocator
	RenderingService pdfRenderingService
	
	@Secured(['ROLE_REVISOR'])
	def verTextosRevision() {
		User usuario = springSecurityService.getCurrentUser()
		def textos = []
		for(texto in usuario.centroEspanol.textos) {
			if(texto.estado == Texto.ESTADO_PENDIENTE && texto.revisor.id == usuario.id && texto.evaluaciones.every {it.estado == Evaluacion.ESTADO_TERMINADA}) {
				textos.add(texto)
			}
		}
		[textos: textos]
	}
	
	@Secured(['ROLE_REVISOR'])
	def verTexto() {
		Texto texto = Texto.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		if(texto && texto.revisor.id == usuario.id) {
			texto.texto = texto.texto.replaceAll('\n', '<br/>')
			MatrizCalificacion matrizCalificacion = texto.evaluaciones[0].matrizCalificacion
			[texto: texto, matrizCalificacion: matrizCalificacion]
		} else {
			flash.error = 'No se encontr\u00F3 el texto seleccionado'
			redirect(action: 'verTextosRevision')
		}
	}

	@Secured(['ROLE_REVISOR'])
	def rechazarEvaluacionTexto() {
		Texto texto = Texto.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		flash.message = ""
		if(texto && texto.revisor.id == usuario.id) {
			texto.evaluaciones.each { evaluacion ->
				RespuestaMatrizCalificacion respuesta = evaluacion.respuestaMatrizCalificacion
				evaluacion.respuestaMatrizCalificacion = null
				respuesta.delete(flush: true)
				evaluacion.numeroRechazos++
				evaluacion.estado = Evaluacion.ESTADO_PENDIENTE
				evaluacion.save(flush: true)
				flash.message += "La evaluaci\u00F3n de ${evaluacion.evaluador} fue rechazada correctamente. <br />"
			}
		} else {
			flash.error = 'No se encontr\u00F3 la evaluaci\u00F3n a rechazar'
		}
			redirect(action: 'verTextosRevision')
	}
	
	@Secured(['ROLE_REVISOR'])
	def aceptarTexto() {
		Texto texto = Texto.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		if(texto && texto.revisor.id == usuario.id) {
			texto.estado = Texto.ESTADO_REVISADO
			texto.save(flush: true)
			flash.message = 'El texto se marc\u00F3 como revisado y se gener\u00F3 el siguiente archivo en formato PDF'
			[texto: texto]
		} else {
			flash.error = 'No se encontr\u00F3 el texto seleccionado'
			redirect(action: 'verTextosRevision')
		}
	}
	
	@Secured(['ROLE_REVISOR'])
	def crearYDesplegarPDF(Texto textoInstance) {
		byte[] bytesImagen = assetResourceLocator.findAssetForURI('logoUniandes.jpg')?.getInputStream()?.bytes
		Date date = new Date()
		String folder = grailsApplication.config.co.edu.uniandes.pdfFolder
		String fileName = date.format("yyyy-MM-d_") + textoInstance.estudiante.usuario.username + ".pdf"
		def respuestas = textoInstance.evaluaciones[0].respuestaMatrizCalificacion.respuestas.sort(false){it.criterio.posicion}
		def respuestasComp
		for(int i = 0; i < textoInstance.evaluaciones.size(); i++) {
			textoInstance.evaluaciones[i].respuestaMatrizCalificacion.respuestas = textoInstance.evaluaciones[i].respuestaMatrizCalificacion.respuestas.sort(false){it.criterio.posicion}
			respuestasComp = textoInstance.evaluaciones[i].respuestaMatrizCalificacion.respuestas
			for(int j = 0; j < respuestasComp.size(); j++) {
				respuestas[j] = respuestas[j].numero < respuestasComp[j].numero? respuestas[j] : respuestasComp[j]
			} 
		}
		textoInstance.evaluaciones[0].respuestaMatrizCalificacion.respuestas = respuestas
		new File(folder + fileName).withOutputStream { outputStream ->
			pdfRenderingService.render([template: '/texto/desplegarPDF', model: [texto: textoInstance, bytesImagen: bytesImagen]], outputStream)
		}
		renderPdf(template: '/texto/desplegarPDF', model: [texto: textoInstance, bytesImagen: bytesImagen])
	}

}
