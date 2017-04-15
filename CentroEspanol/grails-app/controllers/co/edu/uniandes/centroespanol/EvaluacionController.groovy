package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.*

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class EvaluacionController {

    static scaffold = true
	
	def springSecurityService
	
	@Secured(['ROLE_EVALUADOR'])
	def verEvaluacionesPendientes() {
		User usuario = springSecurityService.getCurrentUser()
		def evaluaciones = Evaluacion.findAllByEvaluadorAndEstado(usuario, Evaluacion.ESTADO_PENDIENTE)
		[evaluaciones: evaluaciones]
	}
	
	@Secured(['ROLE_EVALUADOR'])
	def verEvaluacion() {
		Evaluacion evaluacion = Evaluacion.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		if(evaluacion && evaluacion.evaluador.id == usuario.id) {
			evaluacion.texto.texto = evaluacion.texto.texto.replaceAll('\r\n', '<br/>')
			[evaluacion: evaluacion]
		} else {
			flash.error = 'No se encontr\u00F3 la evaluaci\u00F3n seleccionada'
			redirect(action: 'verEvaluacionesPendientes')
		}
	}
	
	@Secured(['ROLE_EVALUADOR'])
	def evaluarTexto() {
		Evaluacion evaluacion = Evaluacion.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		if(evaluacion && evaluacion.evaluador.id == usuario.id && evaluacion.estado == Evaluacion.ESTADO_PENDIENTE) {
			RespuestaMatrizCalificacion respuesta = new RespuestaMatrizCalificacion(respuestas: [], evaluacion: evaluacion)
			for(criterio in evaluacion.matrizCalificacion.criterios) {
				Integer opcionID = params.int("criterio-${criterio.id}")
				if(!opcionID) {
					flash.error = 'No se di\u00F3 respuesta a uno de los criterios'
					return redirect(action: 'verEvaluacion', id: evaluacion.id)
				}
				OpcionCriterio opcion = OpcionCriterio.get(opcionID)
				respuesta.addToRespuestas(opcion)
			}
			if(respuesta.validate()) {
				evaluacion.estado = Evaluacion.ESTADO_TERMINADA
				evaluacion.respuestaMatrizCalificacion
				evaluacion.save(flush: true)
				redirect(action: 'verEvaluacionesPendientes')
			} else {
				flash.error = 'No se pudo guardar la respuesta a la evaluaci\u00F3n'
				redirect(action: 'verEvaluacion', id: evaluacion.id)
			}
		} else {
			flash.error = 'No se encontr\u00F3 la evaluaci\u00F3n seleccionada o esta se encuentra evaluada'
			redirect(action: 'verEvaluacionesPendientes')
		}
	}
	
	@Secured(['ROLE_REVISOR'])
	def rechazarEvaluacion() {
		Evaluacion evaluacion = Evaluacion.get(params.id)
		User usuario = springSecurityService.getCurrentUser()
		if(evaluacion && evaluacion.texto.revisor.id == usuario.id) {
			RespuestaMatrizCalificacion respuesta = evaluacion.respuestaMatrizCalificacion
			evaluacion.respuestaMatrizCalificacion = null
			respuesta.delete(flush: true)
			evaluacion.numeroRechazos++
			evaluacion.estado = Evaluacion.ESTADO_PENDIENTE
			evaluacion.save(flush: true)
			flash.message = "La evaluaci\u00F3n de ${evaluacion.evaluador} fue rechazada correctamente"
			redirect(controller: 'texto', action: 'verTextosRevision')
		} else {
			flash.error = 'No se encontr\u00F3 la evaluaci\u00F3n a rechazar'
		}
	}
}
