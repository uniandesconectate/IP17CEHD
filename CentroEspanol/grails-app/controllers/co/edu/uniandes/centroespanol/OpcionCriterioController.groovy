package co.edu.uniandes.centroespanol

import co.edu.uniandes.login.*

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class OpcionCriterioController {

    static scaffold = true
}
